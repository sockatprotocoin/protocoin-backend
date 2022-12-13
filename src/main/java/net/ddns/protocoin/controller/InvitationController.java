package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.InvitationDTO;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/invite/{userId}")
    public ResponseEntity<InvitationDTO> addInvitationByIds(Authentication authentication, @PathVariable long userId) {
        return ResponseEntity.ok(invitationService.addInvitationByIds(
                ((User)authentication.getPrincipal()).getId(), userId
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvitationById(Authentication authentication, @PathVariable long id) {
        var loggedUserId = ((User)authentication.getPrincipal()).getId();
        var invitation = invitationService.getInvitation(id);
        if (invitation.getUser1().getId() != loggedUserId) {
            return ResponseEntity.badRequest().build();
        }
        invitationService.deleteInvitation(invitation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sent")
    public ResponseEntity<List<InvitationDTO>> getSentInvitation(Authentication authentication) {
        return ResponseEntity.ok(invitationService.getSentInvitations(
                ((User)authentication.getPrincipal()).getId()
        ));
    }

    @GetMapping("/received")
    public ResponseEntity<List<InvitationDTO>> getReceivedInvitation(Authentication authentication) {
        return ResponseEntity.ok(invitationService.getReceivedInvitations(
                ((User)authentication.getPrincipal()).getId()
        ));
    }

    @GetMapping("/{id}/accept")
    public ResponseEntity<List<UserDTO>> acceptInvitationById(Authentication authentication, @PathVariable long id) {
        var loggedUserId = ((User)authentication.getPrincipal()).getId();
        var invitation = invitationService.getInvitation(id);
        if (invitation.getUser2().getId() != loggedUserId) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(invitationService.acceptInvitationAndCreateContact(id));
    }
}
