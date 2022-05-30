package net.ddns.protocoin.dto;

import lombok.Getter;
import net.ddns.protocoin.model.User;

public class UserDTO {
    @Getter
    private long id;
    @Getter
    private String username;
    @Getter
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
