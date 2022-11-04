package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;


@RestController
@RequestMapping("/transaction")
public class TransactionController {


/*



    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;


    @GetMapping("/{transactionId}/")
    ResponseEntity<Transaction> transactionDetails(@PathVariable Integer transactionId){
        // need a method "getTransactionById" from TransactionService that returns a Transaction object.
        if( transactionService.getTransactionById(transactionId) != null ){
            return ResponseEntity.ok( transactionService.transactionById(transactionId).get() );
        } else {
            return ResponseEntity.notFound().build();
        }

}


 // need a method from transactionService that returns a Page<Transaction> collection
    @GetMapping("/{userId}")
    ResponseEntity<Page<Transaction>> transactionsListByUserId(@PathVariable Integer userId , Pageable pageable){
       return ResponseEntity.ok( transactionService.findByUserId(pageable) );
    }


    // probably we need to put the validation dependency in pom.xml --> "javax.validation.Valid";
     need a method "makePayment" from TransactionService wich modifies the currency of the proper Account and saves a
     new Transaction object into the DataBase.

    @PostMapping("/deposit")
    ResponseEntity<?> makeDeposit(@RequestBody @Valid TransactionCreateDTO transDTO ){

     transaction.setType("deposit");
     transactionService.makeTranction(transDTO);
     return new ResponseEntity<>( HttpStatus.CREATED);

    }

   // same as above
    @PostMapping("/payment")
    ResponseEntity<?> makePayment(@RequestBody @Valid TransactionCreateDTO transaction ){

     transaction.setType("payment");
     transactionService.makeTransaction(transaction);
     return new ResponseEntity<>( HttpStatus.CREATED);

    }



    @PatchMapping("/{transactionId}")
    ResponseEntity<?> transactionModification(@PathVariable Integer transactionId , @RequestBody TransactionUpdateDTO transactionUpdateDTO){
             if( !transactionService.transactionById(transactionId).isPresent()){
                 throw new ResourceNotFoundException("transaction which id is  : " + transactionId + " wasn't found");
        }
        transactionService.updateTransaction(transactionUpdateDTO,transactionId);
        return ResponseEntity.ok().build();
         asdasdasdasdasdasdasdasd

    }

*/














}
