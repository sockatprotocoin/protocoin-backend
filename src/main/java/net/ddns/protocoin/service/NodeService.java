package net.ddns.protocoin.service;

import net.ddns.protocoin.communication.connection.socket.Node;
import net.ddns.protocoin.communication.data.Message;
import net.ddns.protocoin.communication.data.ReqType;
import net.ddns.protocoin.core.blockchain.Blockchain;
import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;

@Service
public class NodeService {
    private final Logger logger = LoggerFactory.getLogger(Node.class.getName());
    private final Node node;
    private final BlockChainService blockChainService;
    private final UserService userService;

    @Value("${protocoin.node}")
    private String host;

    @Value("${protocoin.port}")
    private int port;

    public NodeService(Node node, BlockChainService blockChainService, UserService userService) {
        this.node = node;
        this.blockChainService = blockChainService;
        this.userService = userService;
    }

    @PostConstruct
    private void setup() {
        try {
            node.connectToNode(new InetSocketAddress(host, port));
            requestBlockchain();
        } catch (IOException e) {
            logger.info("couldn't find node to connect, creating genesis blockchain and user");
            var createdUser = createGenesisUser();
            logger.info("creating genesis blockchain");
            blockChainService.loadBlockchain(new Blockchain(Converter.hexStringToByteArray(createdUser.getPublicKey())));
            logger.info("blockchain and genesis user created");
        }
    }

    private UserDTO createGenesisUser() {
        var username = "Genesis User";
        var email = "genesis.user@protocoin.ddns.net";
        var password = "supersecret";
        var userToCreate = new User();
        userToCreate.setUsername(username);
        userToCreate.setEmail(email);
        userToCreate.setPassword(password);
        logger.info("creating genesis user ( username: " + username + ", email: " + email + ", password: " + password + ")");
        return userService.addUser(userToCreate);
    }

    public void requestBlockchain() {
        node.broadcast(
                new Message(
                        ReqType.BLOCKCHAIN_REQUEST, new byte[0]
                )
        );
    }

    public void sendTransaction(Transaction transaction) {
        node.broadcast(
                new Message(
                        ReqType.NEW_TRANSACTION, transaction.getBytes()
                )
        );
    }
}
