package net.ddns.protocoin.repository;

import net.ddns.protocoin.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByUser1_IdOrUser2_Id(long userId1, long userId2);
}
