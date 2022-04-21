package net.ddns.protocoin.controller;

import net.ddns.protocoin.model.Contact;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.servivce.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getContacts(@PathVariable long userId) {
        return ResponseEntity.ok(contactService.getContactsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestParam long invitationId) {
        return ResponseEntity.ok(contactService.acceptInvitationAndCreateContact(invitationId));
    }
}
