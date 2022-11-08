package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.transaction.util.DepositStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;


@RestController
@RequestMapping("/transactions")
public class TransactionController {




    @Autowired
    ITransactionService transactionService;



    @GetMapping("/{transactionId}/")
    ResponseEntity<TransactionDTO> transactionDetails(@PathVariable Integer transactionId){

        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));

    }
/*

 // need a method from transactionService that returns a Page<Transaction> collection
    @GetMapping("/{userId}")
    ResponseEntity<Page<Transaction>> transactionsListByUserId(@PathVariable Integer userId , Pageable pageable){
       return ResponseEntity.ok( transactionService.findByUserId(pageable) );
    }


    // probably we need to put the validation dependency in pom.xml --> "javax.validation.Valid";
     need a method "makePayment" from TransactionService wich modifies the currency of the proper Account and saves a
     new Transaction object into the DataBase.
*/
    @PostMapping("/deposit")
    ResponseEntity<?> makeDeposit(@RequestBody @Valid TransactionCreateDTO transDTO ){

     transactionService.makeTransaction(transDTO, new DepositStrategy());
     return new ResponseEntity<>( HttpStatus.CREATED);

    }

   // same as above
    @PostMapping("/payment")
    ResponseEntity<?> makePayment(@RequestBody @Valid TransactionCreateDTO transaction ){

     transactionService.makeTransaction(transaction, new PaymentStrategy());
     return new ResponseEntity<>( HttpStatus.CREATED);

    }
/*


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
