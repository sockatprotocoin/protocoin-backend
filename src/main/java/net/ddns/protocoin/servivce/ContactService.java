package net.ddns.protocoin.servivce;

import net.ddns.protocoin.dto.ContactDTO;
import net.ddns.protocoin.model.Contact;
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

    public List<ContactDTO> getContactsByUserId(long userId) {
        return contactRepository
                .findAllByUser1_IdOrUser2_Id(userId, userId)
                .stream()
                .map(contact -> new ContactDTO(
                        contact.getId(),
                        contact.getUser1().getId() == userId ?
                        contact.getUser2() :
                        contact.getUser1())
                ).collect(Collectors.toList());
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

    public void deleteContact(long id) {
        contactRepository.deleteById(id);
    }
}
