package net.ddns.protocoin.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class HistoryTransactionDTO {
    private final HistoryTransactionType transactionType;
    private final List<TransactionRecord> transactionRecords;

    public HistoryTransactionDTO(HistoryTransactionType transactionType, List<TransactionRecord> transactionRecords) {
        this.transactionType = transactionType;
        this.transactionRecords = transactionRecords;
    }
}
