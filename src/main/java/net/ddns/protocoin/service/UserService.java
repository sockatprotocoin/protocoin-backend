package net.ddns.protocoin.service;

import net.ddns.protocoin.core.ecdsa.Curve;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.core.util.Hash;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.dto.WalletDTO;
import net.ddns.protocoin.model.Role;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.model.Wallet;
import net.ddns.protocoin.repository.RoleRepository;
import net.ddns.protocoin.repository.UserRepository;
import net.ddns.protocoin.service.database.UTXOStorage;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UTXOStorage utxoStorage;
    private final Curve curve;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UTXOStorage utxoStorage, Curve curve, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.utxoStorage = utxoStorage;
        this.curve = curve;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getUser(long id) {
        return userRepository.findById(id).map(UserDTO::new).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    public UserDTO getUser(String username) {
        return userRepository.findByUsername(username).map(UserDTO::new).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public List<UserDTO> getUsersFiltered(String stringFilter) {
        var filteredByUsername = filterUsersAndGetDTOs(
                user -> user.getUsername().toLowerCase().contains(stringFilter.toLowerCase())
        );
        var filteredByEmail = filterUsersAndGetDTOs(
                user -> user.getEmail().toLowerCase().contains(stringFilter.toLowerCase())
        );

        return filteredByUsername.size() > filteredByEmail.size() ? filteredByUsername : filteredByEmail;
    }

    private List<UserDTO> filterUsersAndGetDTOs(Predicate<User> userPredicate) {
        return userRepository.findAll().stream()
                .filter(userPredicate)
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public double getBalance(long userId) {
        var wallet = userRepository.getById(userId).getWallet();
        var utxos = utxoStorage.getUTXOs(Converter.hexStringToByteArray(wallet.getAddress()));
        double balance = 0.0;
        for (var utxo : utxos) {
            balance += utxo.getAmount().toPtc();
        }

        return balance;
    }

    public UserDTO addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var wallet = new Wallet();
        wallet.setPrivateKey(Converter.byteArrayToHexString(curve.privateKey().toByteArray()));
        var publicKey = curve.publicKey(new BigInteger(1, Converter.hexStringToByteArray(wallet.getPrivateKey())));
        wallet.setPublicKey(Converter.byteArrayToHexString(publicKey.toByteArray()));
        wallet.setAddress(Converter.byteArrayToHexString(Hash.ripeMD160(Hash.sha256(publicKey.toByteArray()))));
        wallet.setUser(user);
        user.setWallet(wallet);
        return new UserDTO(userRepository.save(user));
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    public List<UserDTO> getContactsByUserId(long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
            return user.get().getContacts().stream().map(UserDTO::new).collect(Collectors.toList());
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found.");
    }

    public WalletDTO getWalletByUserId(long id) {
        return userRepository.findById(id).map(user -> new WalletDTO(user.getWallet())).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found.")
        );
    }

    public void deleteContact(long id, long contactId) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            Optional<User> contact = userRepository.findById(contactId);
            if(contact.isPresent()){
                user.get().deleteContact(contact.get());
                contact.get().deleteContact(user.get());
                userRepository.saveAll(Arrays.asList(user.get(),contact.get()));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with id: " + contactId);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with id: " + id);
        }
    }

    public void addRoleToUser(String username, String roleName){
        /*
        User user =userRepository.findByUsername(username).orElseThrow(null);
        Role role = roleRepository.findByName(roleName).orElseThrow(null);
        user.addRole(role);
        userRepository.save(user);*/
    }
}
