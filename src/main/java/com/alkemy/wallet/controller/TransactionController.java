package com.alkemy.wallet.controller;


import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping( value = "/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<TransactionDetailDto> getTransactionDetailById(@PathVariable("id") Integer id ) throws Exception {
        return ResponseEntity.ok(transactionService.getTransactionDetailById(id));
    }


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
