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

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<User> contacts = new ArrayList<>();

    public void addContact(User user){
        if(!contacts.contains(user)) {
            contacts.add(user);
        }
    }

    public void deleteContact(User user){
        contacts.remove(user);
    }

    public void addRole(Role role){
        if(!roles.contains(role)){
            roles.add(role);
        }
    }

}
