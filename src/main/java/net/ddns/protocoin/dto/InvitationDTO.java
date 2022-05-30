package net.ddns.protocoin.dto;

import lombok.Getter;
import net.ddns.protocoin.model.Invitation;

public class InvitationDTO {
    @Getter
    private long id;
    @Getter
    private UserDTO inviter;
    @Getter
    private UserDTO invited;

    public InvitationDTO(Invitation invitation) {
        this.id = invitation.getId();
        this.inviter = new UserDTO(invitation.getUser1());
        this.invited = new UserDTO(invitation.getUser2());
    }
}
