package net.ddns.protocoin.dto.blockchain;

import net.ddns.protocoin.core.blockchain.transaction.TransactionInput;
import net.ddns.protocoin.core.util.Converter;

public class TransactionInputDTO {
    private final String txid;
    private final String vout;
    private final String scriptSignature;

    public TransactionInputDTO(TransactionInput transactionInput) {
        txid = Converter.byteArrayToHexString(transactionInput.getTxid().getBytes());
        vout = Converter.byteArrayToHexString(transactionInput.getVout().getBytes());
        scriptSignature = Converter.byteArrayToHexString(transactionInput.getScriptSignature().getBytes());
    }

    public String getTxid() {
        return txid;
    }

    public String getVout() {
        return vout;
    }

    public String getScriptSignature() {
        return scriptSignature;
    }
}
