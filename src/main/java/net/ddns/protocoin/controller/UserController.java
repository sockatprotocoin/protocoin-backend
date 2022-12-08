package net.ddns.protocoin.controller;

import net.ddns.protocoin.config.MyUserDetails;
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

    @GetMapping("/login")
    public ResponseEntity<Void> login(Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(Authentication authentication, @RequestParam(required = false) String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ResponseEntity.ok(userService.getUsers());
        } else {
            return ResponseEntity.ok(userService.getUsersFiltered(stringFilter));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(Authentication authentication) {
        return ResponseEntity.ok(userService.getBalance(
                ((MyUserDetails)authentication.getPrincipal()).getId()
        ));
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUserById(userService.getUser(authentication.getName()).getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contact/{contactId}")
    public ResponseEntity<Void> deleteUser(Authentication authentication, @PathVariable long contactId) {
        userService.deleteContact(((MyUserDetails)authentication.getPrincipal()).getId(), contactId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contact")
    public ResponseEntity<List<UserDTO>> getContactsByUserId(Authentication authentication) {
        return ResponseEntity.ok(userService.getContactsByUserId(((MyUserDetails)authentication.getPrincipal()).getId()));
    }

    @GetMapping("/wallet")
    public ResponseEntity<WalletDTO> getWalletByUserId(Authentication authentication) {
        return ResponseEntity.ok(userService.getWalletByUserId(((MyUserDetails)authentication.getPrincipal()).getId()));
    }
}
