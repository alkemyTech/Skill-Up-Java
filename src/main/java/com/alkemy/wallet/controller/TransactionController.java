package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /*@GetMapping( value = "/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public TransactionDetailDto getTransactionDetailById(@PathVariable Integer id ) throws Exception {
        return transactionService.getTransactionDetailById(id);
    }*/

    //Falta agregar que tire error cuando no este autorizado

    // TODO: Undo comments when model mapper is available
    // TODO: Implement model mapper for TransactionDepositDto
//    @PostMapping( value = "/deposit" )
//    @PreAuthorize("hasRole('USER_ROLE')")
//    public Transaction createDeposit(@RequestBody TransactionDepositDto transactionDepositDto) {
//        return transactionService.createDeposit(convertToEntity(transactionDepositDto));
//    }
}
