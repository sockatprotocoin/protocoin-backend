package net.ddns.protocoin.dto.blockchain;

import net.ddns.protocoin.core.blockchain.block.Block;
import net.ddns.protocoin.core.util.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class BlockDTO {
    private static final String MAGIC_BYTES = "06090609";
    private final String blockHash;
    private final HeaderDTO header;
    private final List<BlockTransactionDTO> blockTransactions;

    public BlockDTO (Block block) {
        blockHash = Converter.byteArrayToHexString(block.getHash());
        header = new HeaderDTO(block.getBlockHeader());
        blockTransactions = block.getTransactions().stream()
                .map(BlockTransactionDTO::new).collect(Collectors.toList());
    }

    public static String getMagicBytes() {
        return MAGIC_BYTES;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public HeaderDTO getHeader() {
        return header;
    }

    public List<BlockTransactionDTO> getBlockTransactions() {
        return blockTransactions;
    }
}
