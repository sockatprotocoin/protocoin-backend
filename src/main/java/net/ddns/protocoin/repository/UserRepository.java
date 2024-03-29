package net.ddns.protocoin.repository;

import net.ddns.protocoin.model.User;
import net.ddns.protocoin.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByWallet(Wallet wallet);
}
