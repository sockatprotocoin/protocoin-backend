package net.ddns.protocoin.dto.blockchain;

import net.ddns.protocoin.core.blockchain.transaction.TransactionOutput;
import net.ddns.protocoin.core.util.Converter;

public class TransactionOutputDTO {
    private final String amount;
    private final String scriptSize;
    private final String script;
    private final String parent;

    public TransactionOutputDTO(TransactionOutput transactionOutput) {
        amount = Converter.byteArrayToHexString(transactionOutput.getAmount().getBytes());
        scriptSize = Converter.byteArrayToHexString(transactionOutput.getScriptSize().getBytes());
        script = Converter.byteArrayToHexString(transactionOutput.getLockingScript().getBytes());
        parent = Converter.byteArrayToHexString(transactionOutput.getParent().getTxId());
    }

    public String getAmount() {
        return amount;
    }

    public String getScriptSize() {
        return scriptSize;
    }

    public String getScript() {
        return script;
    }

    public String getParent() {
        return parent;
    }
}
