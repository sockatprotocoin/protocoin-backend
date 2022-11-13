package net.ddns.protocoin.service;

import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.dto.TransactionDTO;
import net.ddns.protocoin.exception.InsufficientBalanceException;
import net.ddns.protocoin.middleware.TransactionHandler;
import net.ddns.protocoin.repository.UserRepository;
import net.ddns.protocoin.service.database.UTXOStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final UTXOStorage utxoStorage;
    private final TransactionHandler transactionHandler;
    private final NodeService nodeService;

    public TransactionService(
            UserRepository userRepository,
            UTXOStorage utxoStorage,
            TransactionHandler transactionHandler,
            NodeService nodeService
    ) {
        this.userRepository = userRepository;
        this.utxoStorage = utxoStorage;
        this.transactionHandler = transactionHandler;
        this.nodeService = nodeService;
    }

    public void makeTransaction(long idUser, List<TransactionDTO> transactionDTOList) throws InsufficientBalanceException {
        var user = userRepository.getById(idUser);
        var wallet = user.getWallet();
        var utxos = utxoStorage.getUTXOs(Converter.hexStringToByteArray(wallet.getAddress()));

        var transaction = transactionHandler.createTransaction(wallet, utxos,  transactionDTOList);
        sendTransactionToNetwork(transaction);
    }

    private void sendTransactionToNetwork(Transaction transaction) {
        nodeService.sendTransaction(transaction);
    }
}
