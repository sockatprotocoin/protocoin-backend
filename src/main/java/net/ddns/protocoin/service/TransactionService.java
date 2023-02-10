package net.ddns.protocoin.service;

import net.ddns.protocoin.core.blockchain.block.Block;
import net.ddns.protocoin.core.blockchain.data.Bytes;
import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import net.ddns.protocoin.core.blockchain.transaction.TransactionOutput;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.dto.HistoryTransactionDTO;
import net.ddns.protocoin.dto.HistoryTransactionType;
import net.ddns.protocoin.dto.MakeTransactionDTO;
import net.ddns.protocoin.dto.TransactionRecord;
import net.ddns.protocoin.exception.InsufficientBalanceException;
import net.ddns.protocoin.middleware.TransactionHandler;
import net.ddns.protocoin.repository.UserRepository;
import net.ddns.protocoin.repository.WalletRepository;
import net.ddns.protocoin.service.database.UTXOStorage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.ddns.protocoin.dto.HistoryTransactionType.INCOME;
import static net.ddns.protocoin.dto.HistoryTransactionType.OUTCOME;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final UTXOStorage utxoStorage;
    private final TransactionHandler transactionHandler;
    private final NodeService nodeService;
    private final BlockChainService blockChainService;
    private final WalletRepository walletRepository;

    public TransactionService(
            UserRepository userRepository,
            UTXOStorage utxoStorage,
            TransactionHandler transactionHandler,
            NodeService nodeService,
            BlockChainService blockChainService,
            WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.utxoStorage = utxoStorage;
        this.transactionHandler = transactionHandler;
        this.nodeService = nodeService;
        this.blockChainService = blockChainService;
        this.walletRepository = walletRepository;
    }

    public List<HistoryTransactionDTO> getTransactionsForUserId(long userId) {
        var wallet = userRepository.getById(userId).getWallet();
        var publicKey = Bytes.of(Converter.hexStringToByteArray(wallet.getPublicKey()), 64);
        var walletAddress = Converter.hexStringToByteArray(wallet.getAddress());
        var blockchain = blockChainService.getBlockchain();
        var blocks = blockchain.getBlockList();
        return blocks.stream()
                .map(Block::getTransactions)
                .flatMap(Collection::stream)
                .map(transaction -> findUserTransactions(transaction, publicKey, walletAddress))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<HistoryTransactionDTO> findUserTransactions(Transaction transaction, Bytes publicKey, byte[] walletAddress) {
        var listOfFoundUserInputs = transaction.getTransactionInputs().stream()
                .filter(input -> input.getScriptSignature().getPublicKey().equals(publicKey))
                .collect(Collectors.toList());
        var transactionType = listOfFoundUserInputs.size() == 0 ? INCOME : OUTCOME;
        var transactionRecords = transaction.getTransactionOutputs().stream()
                .filter(output -> outputMatchesType(
                        transactionType, Arrays.equals(output.getLockingScript().getReceiver(), walletAddress))
                )
                .map(this::mapTransactionOutputToTransactionRecord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (listOfFoundUserInputs.size() == 0 && transactionRecords.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(new HistoryTransactionDTO(transactionType, transactionRecords));
    }

    private boolean outputMatchesType(HistoryTransactionType type, boolean receiverMatchesWithRequestingUser) {
        if (type.equals(INCOME)) {
            return receiverMatchesWithRequestingUser;
        } else {
            return !receiverMatchesWithRequestingUser;
        }
    }

    private Optional<TransactionRecord> mapTransactionOutputToTransactionRecord(TransactionOutput transactionOutput) {
        var receiverWallet = Converter.byteArrayToHexString(transactionOutput.getLockingScript().getReceiver());
        var walletOptional = walletRepository.findWalletByAddress(receiverWallet);
        if (walletOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new TransactionRecord(transactionOutput.getAmount().toBigInteger(), userRepository.findByWallet(walletOptional.get())));
    }

    public void makeTransaction(long idUser, List<MakeTransactionDTO> makeTransactionDTOList) throws InsufficientBalanceException {
        var user = userRepository.getById(idUser);
        var wallet = user.getWallet();
        var utxos = utxoStorage.getUTXOs(Converter.hexStringToByteArray(wallet.getAddress()));

        var transaction = transactionHandler.createTransaction(wallet, utxos, makeTransactionDTOList);
        nodeService.handleTransaction(transaction);
    }
}
