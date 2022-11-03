package com.alkemy.wallet.controller;

import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // TODO: Implement model mapper for Transaction (TransactionDepositDto)
//    @PostMapping( value = "/deposit" )
//    @PreAuthorize("hasRole('USER_ROLE')")
//    public ResponseEntity<TransactionDepositDto> createDeposit(@RequestBody TransactionDepositDto transactionDepositDto) {
//        try{
//            Transaction depositCreated = transactionService.createDeposit(convertToEntity(transactionDepositDto);
//        } catch (InvalidAmountException err) {
//            handleAmountException(err);
//        }
//
//        return ResponseEntity.ok(convertToDto(depositCreated));
//    }
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Object> handleAmountException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
