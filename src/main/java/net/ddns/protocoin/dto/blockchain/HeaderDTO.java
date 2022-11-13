package net.ddns.protocoin.dto.blockchain;

import net.ddns.protocoin.core.blockchain.block.BlockHeader;
import net.ddns.protocoin.core.util.Converter;

public class HeaderDTO {
    private final String previousBlockHash;
    private final String merkleRoot;
    private final String timestamp;
    private final String targetCompressed;
    private final String nonce;

    public HeaderDTO(BlockHeader blockHeader) {
        this.previousBlockHash = Converter.byteArrayToHexString(blockHeader.getPreviousBlockHash().getBytes());
        this.merkleRoot = Converter.byteArrayToHexString(blockHeader.getMerkleRoot().getBytes());
        this.timestamp = Converter.byteArrayToHexString(blockHeader.getTimestamp().getBytes());
        this.targetCompressed = Converter.byteArrayToHexString(blockHeader.getTargetCompressed().getBytes());
        this.nonce = Converter.byteArrayToHexString(blockHeader.getNonce().getBytes());
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTargetCompressed() {
        return targetCompressed;
    }

    public String getNonce() {
        return nonce;
    }
}
