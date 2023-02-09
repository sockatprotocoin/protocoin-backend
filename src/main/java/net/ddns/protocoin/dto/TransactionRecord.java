package net.ddns.protocoin.dto;

import lombok.Getter;
import net.ddns.protocoin.model.User;

import java.math.BigInteger;

@Getter
public class TransactionRecord {
    private final BigInteger amount;
    private final UserDTO userDTO;

    public TransactionRecord(BigInteger amount, User user) {
        this.amount = amount;
        this.userDTO = new UserDTO(user);
    }
}
