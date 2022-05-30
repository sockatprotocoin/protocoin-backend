package net.ddns.protocoin.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    User user2;
}
