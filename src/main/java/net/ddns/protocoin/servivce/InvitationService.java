package net.ddns.protocoin.servivce;

import net.ddns.protocoin.dto.InvitationDTO;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.model.Invitation;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.repository.InvitationRepository;
import net.ddns.protocoin.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;


    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    public InvitationDTO addInvitation(Invitation invitation) {
        if (invitationRepository.existsByUser1_idAndUser2_id(invitation.getUser1().getId(), invitation.getUser2().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation already exists.");
        }
        return new InvitationDTO(invitationRepository.save(invitation));
    }

    public void deleteInvitation(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

    public void deleteInvitationById(long id) {
        invitationRepository.deleteById(id);
    }

    public List<InvitationDTO> getSentInvitations(long userId) {
        return invitationRepository.findAllByUser1_id(userId).stream().map(InvitationDTO::new).collect(Collectors.toList());
    }

    public List<InvitationDTO> getReceivedInvitations(long userId) {
        return invitationRepository.findAllByUser2_id(userId).stream().map(InvitationDTO::new).collect(Collectors.toList());
    }

    public List<UserDTO> acceptInvitationAndCreateContact(long invitationId) {
        var invitation = invitationRepository.findById(invitationId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invitation found.");
        });

        User user1 = invitation.getUser1();
        User user2 = invitation.getUser2();

        user1.addContact(invitation.getUser2());
        user2.addContact(invitation.getUser1());

        deleteInvitation(invitation);

        return userRepository.saveAll(Arrays.asList(invitation.getUser1(), invitation.getUser2())).stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public InvitationDTO addInvitationByIds(long inviterId, long invitedId) {
        if (invitationRepository.existsByUser1_idAndUser2_id(inviterId, invitedId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation already exists.");
        }

        Optional<User> user1 = userRepository.findById(inviterId);
        Optional<User> user2 = userRepository.findById(invitedId);

        if(user1.isPresent()){
            if(user2.isPresent()){
                Invitation invitation = new Invitation();
                invitation.setUser1(user1.get());
                invitation.setUser2(user2.get());
                return new InvitationDTO(invitationRepository.save(invitation));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + invitedId + " does not exist.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + inviterId + " does not exist.");
        }
    }
}
