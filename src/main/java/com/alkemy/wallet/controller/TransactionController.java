package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final ITransactionService transactionService;

    private TransactionDTO transactionDTO;

    @PostMapping(value = "/sendArs")
    public ResponseEntity<TransactionDTO> createTransactionArs(@Valid @RequestBody TransactionDTO transactionDTO) throws BankException {
        return new ResponseEntity<>(transactionService.createTransactionArs(transactionDTO),transactionDTO.getUserId()), HttpStatus.OK);

    }

    @GetMapping("/transactions/user/{id}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByUser(@PathVariable Long id) throws BankException {

        return new ResponseEntity<>(TransactionDTO.mapToDto(transactionService.getAllTransactionByUser(id),id), HttpStatus.OK);

    }
}

}
*/
