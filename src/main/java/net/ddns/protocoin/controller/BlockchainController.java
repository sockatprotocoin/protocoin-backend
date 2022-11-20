package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.blockchain.BlockDTO;
import net.ddns.protocoin.middleware.BlockchainHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/blockchain")
public class BlockchainController {
    private final BlockchainHandler blockchainHandler;

    public BlockchainController(BlockchainHandler blockchainHandler) {
        this.blockchainHandler = blockchainHandler;
    }

    @GetMapping
    public ResponseEntity<List<BlockDTO>> getBlockchain() {
        return ResponseEntity.ok(blockchainHandler.getBlockchain());
    }
}
