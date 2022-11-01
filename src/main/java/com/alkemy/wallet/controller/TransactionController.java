package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDetailDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    //private final TransactionService transactionService;

    /*@GetMapping( value = "/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public TransactionDetailDto getTransactionDetailById(@PathVariable Integer id ) throws Exception {
        return transactionService.getTransactionDetailById(id);
    }*/

    //Falta agregar que tire error cuando no este autorizado

}
