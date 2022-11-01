package net.ddns.protocoin.controller;

import net.ddns.protocoin.dto.TransactionDTO;
import net.ddns.protocoin.exception.InsufficientBalanceException;
import net.ddns.protocoin.servivce.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transaction")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{idUser}")
    public ResponseEntity<Void> newTransaction(
            @PathVariable long idUser,
            @RequestBody List<TransactionDTO> transactionDTOList
    ) {
        try {
            transactionService.makeTransaction(idUser, transactionDTOList);
        } catch (InsufficientBalanceException e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
