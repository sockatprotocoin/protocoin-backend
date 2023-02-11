package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.dto.WalletDTO;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/usersNotInContact")
    public ResponseEntity<List<UserDTO>> getUsersNotInContact(Authentication authentication, @RequestParam(required = false) String stringFilter) {
        var userId = ((User)authentication.getPrincipal()).getId();
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ResponseEntity.ok(userService.getUsersNotInContact(userId));
        } else {
            return ResponseEntity.ok(userService.getUsersNotInContactFiltered(stringFilter, userId));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(Authentication authentication) {
        var userId = ((User)authentication.getPrincipal()).getId();
        return ResponseEntity.ok(userService.getBalance(userId));
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUserById(((User)authentication.getPrincipal()).getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contact/{contactId}")
    public ResponseEntity<Void> deleteUser(Authentication authentication, @PathVariable long contactId) {
        userService.deleteContact(((User)authentication.getPrincipal()).getId(), contactId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contact")
    public ResponseEntity<List<UserDTO>> getContactsByUserId(Authentication authentication) {
        return ResponseEntity.ok(userService.getContactsByUserId((((User)authentication.getPrincipal()).getId())));
    }

    @GetMapping("/wallet")
    public ResponseEntity<WalletDTO> getWalletByUserId(Authentication authentication) {
        return ResponseEntity.ok(userService.getWalletByUserId((((User)authentication.getPrincipal()).getId())));
    }
}
