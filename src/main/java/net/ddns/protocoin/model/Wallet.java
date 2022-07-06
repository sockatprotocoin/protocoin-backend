package net.ddns.protocoin.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String privateKey;

    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
