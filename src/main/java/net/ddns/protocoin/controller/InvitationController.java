package net.ddns.protocoin.controller;

import net.ddns.protocoin.model.Invitation;
import net.ddns.protocoin.servivce.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invitations")
@CrossOrigin(origins = "http://localhost:3000")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<Invitation> addInvitation(@RequestBody Invitation invitation) {
        return ResponseEntity.ok(invitationService.addInvitation(invitation));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInvitation(@RequestBody Invitation invitation) {
        invitationService.deleteInvitation(invitation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sent")
    public ResponseEntity<List<Invitation>> getSentInvitation(@RequestParam long userId) {
        return ResponseEntity.ok(invitationService.getSentInvitations(userId));
    }

    @GetMapping("/received")
    public ResponseEntity<List<Invitation>> getReceivedInvitation(@RequestParam long userId) {
        return ResponseEntity.ok(invitationService.getReceivedInvitations(userId));
    }
}
