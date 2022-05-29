package net.ddns.protocoin.controller;

import net.ddns.protocoin.model.User;
import net.ddns.protocoin.servivce.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }
}
