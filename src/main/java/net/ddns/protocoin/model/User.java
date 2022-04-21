package net.ddns.protocoin.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany
    List<User> contacts = new ArrayList<>();

    @OneToMany
    List<User> sentInvitations = new ArrayList<>();

    @OneToMany
    List<User> receivedInvitations = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }


    public List<User> getSentInvitations() {
        return sentInvitations;
    }

    public void setSentInvitations(List<User> sentInvitations) {
        this.sentInvitations = sentInvitations;
    }

    public List<User> getReceivedInvitations() {
        return receivedInvitations;
    }

    public void setReceivedInvitations(List<User> receivedInvitations) {
        this.receivedInvitations = receivedInvitations;
    }
}
