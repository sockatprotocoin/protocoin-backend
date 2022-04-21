package net.ddns.protocoin.servivce;

import net.ddns.protocoin.model.Invitation;
import net.ddns.protocoin.repository.InvitationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public Invitation addInvitation(Invitation invitation) {
        if (invitationRepository.existsByUser1_idAndUser2_id(invitation.getUser1().getId(), invitation.getUser2().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation already exists.");
        }
        return invitationRepository.save(invitation);
    }

    public void deleteInvitation(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

    public List<Invitation> getSentInvitations(long userId) {
        return invitationRepository.findAllByUser1_id(userId);
    }

    public List<Invitation> getReceivedInvitations(long userId) {
        return invitationRepository.findAllByUser2_id(userId);
    }
}
