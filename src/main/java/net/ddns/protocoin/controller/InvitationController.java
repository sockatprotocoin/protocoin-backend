package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.InvitationDTO;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.model.Invitation;
import net.ddns.protocoin.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invitation")
@CrossOrigin(origins = "http://localhost:3000")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationDTO> addInvitation(@RequestBody Invitation invitation) {
        return ResponseEntity.ok(invitationService.addInvitation(invitation));
    }

    @PostMapping("/invite")
    public ResponseEntity<InvitationDTO> addInvitationByIds(@RequestParam long inviterId, @RequestParam long invitedId) {
        return ResponseEntity.ok(invitationService.addInvitationByIds(inviterId,invitedId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInvitation(@RequestBody Invitation invitation) {
        invitationService.deleteInvitation(invitation);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvitationById(@PathVariable long id) {
        invitationService.deleteInvitationById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sent")
    public ResponseEntity<List<InvitationDTO>> getSentInvitation(@RequestParam long userId) {
        return ResponseEntity.ok(invitationService.getSentInvitations(userId));
    }

    @GetMapping("/received")
    public ResponseEntity<List<InvitationDTO>> getReceivedInvitation(@RequestParam long userId) {
        return ResponseEntity.ok(invitationService.getReceivedInvitations(userId));
    }

    @GetMapping("/accept/{id}")
    public ResponseEntity<List<UserDTO>> acceptInvitationById(@PathVariable long id) {
        return ResponseEntity.ok(invitationService.acceptInvitationAndCreateContact(id));
    }
}
