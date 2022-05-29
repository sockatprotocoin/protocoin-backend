package net.ddns.protocoin.dto;

import lombok.Getter;
import net.ddns.protocoin.model.User;

public class ContactDTO {
    @Getter
    private final long id;
    @Getter
    private final User user;

    public ContactDTO(long id, User user) {
        this.id = id;
        this.user = user;
    }
}
