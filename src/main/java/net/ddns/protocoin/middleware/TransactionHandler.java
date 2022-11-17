package net.ddns.protocoin.middleware;

import net.ddns.protocoin.core.blockchain.data.Satoshi;
import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import net.ddns.protocoin.core.blockchain.transaction.TransactionInput;
import net.ddns.protocoin.core.blockchain.transaction.TransactionOutput;
import net.ddns.protocoin.core.blockchain.transaction.signature.PayToPubKeyHash;
import net.ddns.protocoin.core.blockchain.transaction.signature.ScriptSignature;
import net.ddns.protocoin.core.ecdsa.Curve;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.core.util.Hash;
import net.ddns.protocoin.dto.TransactionDTO;
import net.ddns.protocoin.exception.InsufficientBalanceException;
import net.ddns.protocoin.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class TransactionHandler {
    private final Curve curve;

    public TransactionHandler(Curve curve) {
        this.curve = curve;
    }

    public Transaction createTransaction(Wallet wallet, List<TransactionOutput> utxos, List<TransactionDTO> transactionDTOList) throws InsufficientBalanceException {
        var transactionOutputs = new ArrayList<TransactionOutput>();
        double totalAmount = 0;
        for (TransactionDTO transactionDTO : transactionDTOList) {
            totalAmount += transactionDTO.getAmount();
            transactionOutputs.add(createTransactionOutputFromTransactionDTO(transactionDTO));
        }

        var transactionInputs = new ArrayList<TransactionInput>();
        double spentOutputsAmount = 0.0;
        utxos.sort(Comparator.comparing(TransactionOutput::getAmount));
        for (int i = 0; i < utxos.size() && totalAmount > spentOutputsAmount; i++) {
            var utxo = utxos.get(i);
            transactionInputs.add(
                    createTransactionInputFromTransactionOutput(utxo)
            );
            spentOutputsAmount += utxo.getAmount().toPtc();
        }
        if (totalAmount > spentOutputsAmount) {
            throw new InsufficientBalanceException(
                    "Your balance: " + spentOutputsAmount + " is below transaction total amount: " + totalAmount
            );
        }
        if (totalAmount < spentOutputsAmount) {
            transactionOutputs.add(new TransactionOutput(
                    Satoshi.valueOf(spentOutputsAmount - totalAmount), PayToPubKeyHash.fromPublicKey(wallet.getPublicKeyBytes())
            ));
        }

        var transaction = new Transaction(transactionInputs, transactionOutputs);
        var transactionDataToSign = Hash.sha256(transaction.getBytesWithoutSignatures());
        var signature = curve.sign(new BigInteger(1, wallet.getPrivateKeyBytes()), transactionDataToSign);
        var scriptSignature = new ScriptSignature(signature.getBytes(), wallet.getPublicKeyBytes());
        transaction.getTransactionInputs().forEach(input -> input.setScriptSignature(scriptSignature));

        return transaction;
    }

    private TransactionInput createTransactionInputFromTransactionOutput(TransactionOutput transactionOutput) {
        return new TransactionInput(
                transactionOutput.getParent().getTxId(), transactionOutput.getVout().getBytes()
        );
    }

    private TransactionOutput createTransactionOutputFromTransactionDTO(TransactionDTO transactionDTO) {
        var lockingScript = PayToPubKeyHash.fromPubKeyHash(Converter.hexStringToByteArray(transactionDTO.getReceiverWalletAddress()));
        return new TransactionOutput(Satoshi.valueOf(transactionDTO.getAmount()), lockingScript);
    }
}
