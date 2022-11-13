package net.ddns.protocoin.service;

import net.ddns.protocoin.communication.connection.socket.Node;
import net.ddns.protocoin.communication.data.Message;
import net.ddns.protocoin.communication.data.ReqType;
import net.ddns.protocoin.core.blockchain.transaction.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;

@Service
public class NodeService {
    private final Node node;

    @Value("${protocoin.node}")
    private String host;

    @Value("${protocoin.port}")
    private int port;

    public NodeService(Node node) {
        this.node = node;
    }

    @PostConstruct
    private void setup() {
        try {
            node.connectToNode(new InetSocketAddress(host, port));
        } catch (IOException e) {
            //TODO: should close spring application
        }
        requestBlockchain();
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
