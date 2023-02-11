package net.ddns.protocoin.dto;

import lombok.Getter;
import net.ddns.protocoin.model.User;

@Getter
public class TransactionRecord {
    private final double amount;
    private final UserDTO recipient;

    public TransactionRecord(double amount, User user) {
        this.amount = amount;
        this.recipient = new UserDTO(user);
    }
}
