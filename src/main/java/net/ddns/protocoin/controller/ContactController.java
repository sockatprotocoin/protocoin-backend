package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.ContactDTO;
import net.ddns.protocoin.model.Contact;
import net.ddns.protocoin.servivce.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ContactDTO>> getContacts(@PathVariable long id) {
        return ResponseEntity.ok(contactService.getContactsByUserId(id));
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestParam long invitationId) {
        return ResponseEntity.ok(contactService.acceptInvitationAndCreateContact(invitationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
}
