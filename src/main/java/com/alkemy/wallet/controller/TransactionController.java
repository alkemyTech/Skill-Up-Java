package com.alkemy.wallet.controller;



import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDepositRequestDto;
import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.dto.TransactionPatchDto;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/detail/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<TransactionDetailDto> getTransactionDetailById(@PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(transactionService.getTransactionDetailById(id));
    }

    @PostMapping( value = "/deposit" )
    public ResponseEntity<TransactionDepositDto> createDeposit(@RequestBody TransactionDepositRequestDto transactionDepositRequestDto) {
        return ResponseEntity.ok(transactionService.createDeposit(transactionDepositRequestDto));
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Object> handleAmountException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping(value = "/all/{userId}")
//    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<List<TransactionDetailDto>> listTransactions(@PathVariable Integer userId) {
        return ResponseEntity.ok(transactionService.getTransactions(userId));
    }

    @PatchMapping(value="/{id}")
    ResponseEntity<TransactionDetailDto> updateTransaction(@RequestBody TransactionPatchDto transactionPatchDto, @PathVariable Integer id) throws Exception{
        return ResponseEntity.ok(transactionService.updateTransaction(transactionPatchDto,id));
    }

}
