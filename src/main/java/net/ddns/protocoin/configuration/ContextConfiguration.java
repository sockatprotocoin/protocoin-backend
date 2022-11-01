package net.ddns.protocoin.configuration;

import net.ddns.protocoin.communication.connection.socket.Node;
import net.ddns.protocoin.core.ecdsa.Curve;
import net.ddns.protocoin.service.ProtocoinContext;
import net.ddns.protocoin.service.database.UTXOStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration {
    private final ProtocoinContext protocoinContext = new ProtocoinContext();

    @Bean
    public ProtocoinContext getProtocoinContext() {
        return protocoinContext;
    }

    @Bean
    public Curve getCurve() {
        return protocoinContext.getCurve();
    }

    @Bean
    public UTXOStorage getUTXOStorage() {
        return protocoinContext.getUtxoStorage();
    }

    @Bean
    public Node getNode() {
        return protocoinContext.getNode();
    }
}
