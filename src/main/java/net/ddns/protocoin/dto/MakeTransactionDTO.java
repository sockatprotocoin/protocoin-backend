package net.ddns.protocoin.dto;

public class MakeTransactionDTO {
    private final String receiverWalletAddress;
    private final double amount;

    public MakeTransactionDTO(String receiverWalletAddress, double amount) {
        this.receiverWalletAddress = receiverWalletAddress;
        this.amount = amount;
    }

    public String getReceiverWalletAddress() {
        return receiverWalletAddress;
    }

    public double getAmount() {
        return amount;
    }
}
