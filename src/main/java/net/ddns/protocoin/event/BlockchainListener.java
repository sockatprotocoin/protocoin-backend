package net.ddns.protocoin.event;

import net.ddns.protocoin.communication.connection.socket.Node;
import net.ddns.protocoin.eventbus.event.NewBlockEvent;
import net.ddns.protocoin.eventbus.listener.Listener;
import net.ddns.protocoin.service.BlockChainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class BlockchainListener extends Listener<NewBlockEvent> {
    private final Logger logger = LoggerFactory.getLogger(Node.class.getName());
    private BlockChainService blockChainService;

    @Value("${protocoin.blockchain.path:blockchain}")
    private String blockchainLocation;

    public void setBlockChainService(BlockChainService blockChainService) {
        this.blockChainService = blockChainService;
    }

    @Override
    protected void handle(NewBlockEvent newBlockEvent) {
        logger.info("blockchain has new block");
        try {
            Files.write(Path.of(blockchainLocation), blockChainService.getBlockchain().getBytes());
        } catch (IOException e) {
            logger.error("field writing blockchain to: " + blockchainLocation);
        }
        logger.info("done writing blockchain to: " + blockchainLocation);
    }
}
