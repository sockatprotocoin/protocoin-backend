package net.ddns.protocoin.servivce;

import net.ddns.protocoin.model.User;
import net.ddns.protocoin.model.Wallet;
import net.ddns.protocoin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        var wallet = new Wallet();
        wallet.setAddress("address");
        wallet.setPrivateKey("privateKey");
        wallet.setUser(user);
        user.setWallet(wallet);
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
