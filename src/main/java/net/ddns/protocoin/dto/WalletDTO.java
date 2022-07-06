package net.ddns.protocoin.dto;

import lombok.Getter;
import net.ddns.protocoin.model.Wallet;

public class WalletDTO {
    @Getter
    private long id;
    @Getter
    private String address;

    public WalletDTO(Wallet wallet) {
        this.id = wallet.getId();
        this.address = wallet.getAddress();
    }
}
