package net.ddns.protocoin.dto;

public class TransactionDTO {
    private final String walletAddress;
    private final double amount;

    public TransactionDTO(String walletAddress, double amount) {
        this.walletAddress = walletAddress;
        this.amount = amount;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public double getAmount() {
        return amount;
    }
}
