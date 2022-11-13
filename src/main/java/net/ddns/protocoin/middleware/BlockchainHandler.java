package net.ddns.protocoin.middleware;

import net.ddns.protocoin.dto.blockchain.BlockDTO;
import net.ddns.protocoin.service.BlockChainService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlockchainHandler {
    private final BlockChainService blockChainService;

    public BlockchainHandler(BlockChainService blockChainService) {
        this.blockChainService = blockChainService;
    }

    public List<BlockDTO> getBlockchain() {
        return blockChainService.getBlockchain().getBlockchain().stream().map(BlockDTO::new).collect(Collectors.toList());
    }
}
