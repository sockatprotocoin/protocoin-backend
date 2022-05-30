package net.ddns.protocoin.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String email;

    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<User> contacts = new ArrayList<>();

    public void addContact(User user){
        if(!contacts.contains(user)) {
            contacts.add(user);
        }
    }

    public void deleteContact(User user){
        contacts.remove(user);
    }
}
