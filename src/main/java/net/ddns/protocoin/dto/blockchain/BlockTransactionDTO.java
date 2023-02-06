package net.ddns.protocoin.dto.blockchain;

import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import net.ddns.protocoin.core.util.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class BlockTransactionDTO {
    private final List<TransactionInputDTO> transactionInputs;
    private final List<TransactionOutputDTO> transactionOutputs;

    private final String txId;

    public BlockTransactionDTO(Transaction transaction) {
        txId = Converter.byteArrayToHexString(transaction.getTxId());
        transactionInputs = transaction.getTransactionInputs().stream().map(TransactionInputDTO::new).collect(Collectors.toList());
        transactionOutputs = transaction.getTransactionOutputs().stream().map(TransactionOutputDTO::new).collect(Collectors.toList());
    }

    public List<TransactionInputDTO> getTransactionInputs() {
        return transactionInputs;
    }

    public List<TransactionOutputDTO> getTransactionOutputs() {
        return transactionOutputs;
    }

    public String getTxId() {return txId; }
}
