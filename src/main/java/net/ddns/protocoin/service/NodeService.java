package net.ddns.protocoin.service;

import net.ddns.protocoin.communication.connection.socket.Node;
import net.ddns.protocoin.communication.data.Message;
import net.ddns.protocoin.communication.data.ReqType;
import net.ddns.protocoin.core.blockchain.Blockchain;
import net.ddns.protocoin.core.blockchain.block.BlockDataException;
import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import net.ddns.protocoin.core.util.Converter;
import net.ddns.protocoin.dto.UserDTO;
import net.ddns.protocoin.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class NodeService {
    private final Logger logger = LoggerFactory.getLogger(NodeService.class.getName());
    private final Node node;
    private final BlockChainService blockChainService;
    private final MiningService miningService;
    private final UserService userService;

    @Value("${protocoin.node:}")
    private String host;

    @Value("${protocoin.port}")
    private int port;

    @Value("${protocoin.blockchain.path:blockchain}")
    private String blockchainLocation;

    public NodeService(Node node, BlockChainService blockChainService, MiningService miningService, UserService userService) {
        this.node = node;
        this.blockChainService = blockChainService;
        this.miningService = miningService;
        this.userService = userService;
    }

    @PostConstruct
    private void setup() {
        if (!host.isEmpty() && 65536 > port && port > 0) {
            try {
                networkStartup();
                return;
            } catch (IOException e) {
                logger.info("couldn't find node to connect, creating genesis blockchain and user");
            }
        }
        var blockchainPath = Path.of(blockchainLocation);
        if (Files.exists(blockchainPath) && !blockchainLocation.isEmpty()) {
            try {
                localStartup(blockchainPath);
                return;
            } catch (IOException | BlockDataException e) {
                logger.info("failed reading blockchain from file (it might be corrupted or invalid)");
            }
        }
        genesisRun();
    }

    private void networkStartup() throws IOException {
        logger.info("connecting to network");
        node.connectToNode(new InetSocketAddress(host, port));
        logger.info("retrieving blockchain update from network");
        requestBlockchain();
    }

    private void localStartup(Path blockchainPath) throws IOException, BlockDataException {
        blockChainService.loadBlockchain(
                Blockchain.readFromInputStream(
                        new FileInputStream(blockchainPath.toFile())
                )
        );
        logger.info("blockchain loaded");
        node.startMining(1);
        logger.info("mining started");
    }

    private void genesisRun() {
        var createdUser = createGenesisUser();
        logger.info("creating genesis blockchain");
        blockChainService.loadBlockchain(new Blockchain(Converter.hexStringToByteArray(createdUser.getPublicKey())));
        logger.info("blockchain and genesis user created");
        node.startMining(1);
        logger.info("mining started");
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

    public void handleTransaction(Transaction transaction) {
        if (node.connectedNetworkSize() > 0) {
            node.broadcast(
                    new Message(
                            ReqType.NEW_TRANSACTION, transaction.getBytes()
                    )
            );
        } else {
            miningService.registerNewTransaction(transaction);
        }
    }
}
