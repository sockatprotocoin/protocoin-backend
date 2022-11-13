package net.ddns.protocoin.model;

import lombok.Data;
import net.ddns.protocoin.core.util.Converter;

import javax.persistence.*;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String privateKey;
    private String publicKey;
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public byte[] getPrivateKeyBytes() {
        return Converter.hexStringToByteArray(privateKey);
    }

    public byte[] getPublicKeyBytes() {
        return Converter.hexStringToByteArray(publicKey);
    }

    public byte[] getAddressBytes() {
        return Converter.hexStringToByteArray(address);
    }
}
