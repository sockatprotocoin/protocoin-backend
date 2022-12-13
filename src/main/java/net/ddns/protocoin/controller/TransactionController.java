package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.TransactionDTO;
import net.ddns.protocoin.exception.InsufficientBalanceException;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    public ResponseEntity<Void> newTransaction(
            Authentication authentication,
            @RequestBody List<TransactionDTO> transactionDTOList
    ) {
        try {
            transactionService.makeTransaction(
                    ((User)authentication.getPrincipal()).getId(), transactionDTOList
            );
        } catch (InsufficientBalanceException e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
