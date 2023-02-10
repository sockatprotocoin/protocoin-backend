package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.HistoryTransactionDTO;
import net.ddns.protocoin.dto.MakeTransactionDTO;
import net.ddns.protocoin.exception.InsufficientBalanceException;
import net.ddns.protocoin.model.User;
import net.ddns.protocoin.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<List<HistoryTransactionDTO>> userTransactionHistory(Authentication authentication) {
        var userId = ((User)authentication.getPrincipal()).getId();
        return ResponseEntity.ok(transactionService.getTransactionsForUserId(userId));
    }

    @PostMapping()
    public ResponseEntity<Void> newTransaction(
            Authentication authentication,
            @RequestBody List<MakeTransactionDTO> makeTransactionDTOList
    ) {
        try {
            transactionService.makeTransaction(
                    ((User)authentication.getPrincipal()).getId(), makeTransactionDTOList
            );
        } catch (InsufficientBalanceException e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
