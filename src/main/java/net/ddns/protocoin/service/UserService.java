package net.ddns.protocoin.service;

import net.ddns.protocoin.core.ecdsa.Curve;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.core.util.Hash;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.dto.WalletDTO;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.model.Wallet;
import net.ddns.protocoin.repository.UserRepository;
import net.ddns.protocoin.service.database.UTXOStorage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UTXOStorage utxoStorage;
    private final Curve curve;

    public UserService(UserRepository userRepository, UTXOStorage utxoStorage, Curve curve) {
        this.userRepository = userRepository;
        this.utxoStorage = utxoStorage;
        this.curve = curve;
    }

    public UserDTO getUser(long id) {
        return userRepository.findById(id).map(UserDTO::new).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
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
}
