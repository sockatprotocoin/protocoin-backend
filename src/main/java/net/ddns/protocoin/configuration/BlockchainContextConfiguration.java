package net.ddns.protocoin.configuration;

import net.ddns.protocoin.communication.connection.socket.Node;
import net.ddns.protocoin.core.ecdsa.Curve;
import net.ddns.protocoin.core.script.ScriptInterpreter;
import net.ddns.protocoin.eventbus.EventBus;
import net.ddns.protocoin.service.BlockChainService;
import net.ddns.protocoin.service.MiningService;
import net.ddns.protocoin.service.database.UTXOStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class BlockchainContextConfiguration {
    private EventBus eventBus;
    private Curve curve;
    private ScriptInterpreter scriptInterpreter;
    private UTXOStorage utxoStorage;
    private BlockChainService blockChainService;
    private MiningService miningService;
    private Node node;

    private <T> T instantiate(T t, Supplier<T> creator) {
        if (t == null) {
            t = creator.get();
        }
        return t;
    }

    @Bean
    public Curve getCurve() {
        return instantiate(curve, () -> Curve.secp256k1);
    }

    @Bean
    public EventBus getEventBus() {
        return instantiate(eventBus, EventBus::new);
    }

    @Bean
    public ScriptInterpreter getScriptInterpreter(Curve curve) {
        return instantiate(scriptInterpreter, () -> new ScriptInterpreter(curve));
    }

    @Bean
    public UTXOStorage getUTXOStorage(ScriptInterpreter scriptInterpreter) {
        return instantiate(utxoStorage, () -> new UTXOStorage(scriptInterpreter));
    }

    @Bean
    public BlockChainService blockChainService(UTXOStorage utxoStorage, EventBus eventBus) {
        return instantiate(blockChainService, () -> new BlockChainService(utxoStorage, eventBus));
    }

    @Bean
    public MiningService getMiningService(UTXOStorage utxoStorage, BlockChainService blockChainService, EventBus eventBus) {
        return instantiate(miningService, () -> new MiningService(utxoStorage, blockChainService, eventBus));
    }

    @Bean
    public Node getNode(MiningService miningService, EventBus eventBus) {
        return instantiate(node, () -> new Node(miningService, eventBus));
    }
}
