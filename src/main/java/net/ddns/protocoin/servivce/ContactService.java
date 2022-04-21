package net.ddns.protocoin.servivce;

import net.ddns.protocoin.model.Contact;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.repository.ContactRepository;
import net.ddns.protocoin.repository.InvitationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    private final InvitationRepository invitationRepository;

    public ContactService(ContactRepository contactRepository, InvitationRepository invitationRepository) {
        this.contactRepository = contactRepository;
        this.invitationRepository = invitationRepository;
    }

    public List<User> getContactsByUserId(long userId) {
        return contactRepository
                .findAllByUser1_IdOrUser2_Id(userId, userId)
                .stream().map(contact ->
                        contact.getUser2().getId() == userId ?
                        contact.getUser1() :
                        contact.getUser2()).collect(Collectors.toList()
                );
    }

    public Contact acceptInvitationAndCreateContact(long invitationId) {
        var invitation = invitationRepository.findById(invitationId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No invitation found.");
        });

        var contact = new Contact();
        contact.setUser1(invitation.getUser1());
        contact.setUser2(invitation.getUser2());

        invitationRepository.delete(invitation);
        return contactRepository.save(contact);
    }
}
