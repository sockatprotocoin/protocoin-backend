package net.ddns.protocoin.controller;

import net.ddns.protocoin.service.NodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.InetSocketAddress;
import java.util.List;

@Controller
@RequestMapping("/network")
public class NetworkController {
    private final NodeService nodeService;

    public NetworkController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public ResponseEntity<List<InetSocketAddress>> getNetwork() {
        return ResponseEntity.ok(nodeService.getNodesAddresses());
    }
}
