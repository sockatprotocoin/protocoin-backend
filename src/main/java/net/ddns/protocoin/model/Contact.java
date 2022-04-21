package net.ddns.protocoin.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Contact {
    @Id
    private long id;

    @ManyToOne()
    User user1;

    @ManyToOne()
    User user2;
}
