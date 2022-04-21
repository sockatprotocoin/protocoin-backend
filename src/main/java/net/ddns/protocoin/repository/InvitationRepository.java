package net.ddns.protocoin.repository;

import net.ddns.protocoin.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findAllByUser1_id(long senderId);
    List<Invitation> findAllByUser2_id(long receiverId);
    boolean existsByUser1_idAndUser2_id(long senderId, long receiverId);
}
