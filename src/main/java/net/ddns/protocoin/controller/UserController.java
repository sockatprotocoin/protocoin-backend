package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.dto.WalletDTO;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable long id) {
        return ResponseEntity.ok(userService.getBalance(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/contact/{contactId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id, @PathVariable long contactId) {
        userService.deleteContact(id,contactId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/contact")
    public ResponseEntity<List<UserDTO>> getContactsByUserId(@PathVariable long id) {
        return ResponseEntity.ok(userService.getContactsByUserId(id));
    }

    @GetMapping("/{id}/wallet")
    public ResponseEntity<WalletDTO> getWalletByUserId(@PathVariable long id) {
        return ResponseEntity.ok(userService.getWalletByUserId(id));
    }
}
