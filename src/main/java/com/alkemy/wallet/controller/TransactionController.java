package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.PageDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.enumeration.TypeTransaction;
import com.alkemy.wallet.mapper.exception.ParamNotFound;
import com.alkemy.wallet.service.ITransactionService;
import java.security.PublicKey;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

  @Autowired
  RestExceptionHandler restExceptionHandler;
  @Autowired
  private ITransactionService transactionService;
  @GetMapping("/user")
  public ResponseEntity<PageDto<TransactionDto>> getAllTransactionsByUserId(@PageableDefault(size = 10 ) Pageable page, HttpServletRequest request, @RequestParam Long id)
  {
    PageDto<TransactionDto> pageDto = transactionService.findAllTransaction(page,request,id);
    return ResponseEntity.ok().body(pageDto);

  }
  @PostMapping("/payment")
  public ResponseEntity<TransactionDto> makeAPayment(@RequestBody TransactionDto dto) {

      dto.setType(TypeTransaction.PAYMENT);
      TransactionDto transactionDto = transactionService.createTransaction(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(transactionDto);
    }
    @GetMapping("/{transactionId}")

    public ResponseEntity<TransactionDto> getDetailTransaction(@PathVariable Long transactionId){
      TransactionDto transaction = this.transactionService.getDetailById(transactionId);
      return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long id, @RequestBody TransactionDto transactionDto) {
      TransactionDto transactionUpdatedDto = transactionService.refreshValues(id, transactionDto);
      return ResponseEntity.ok().body(transactionUpdatedDto);
    }


  @PostMapping("/transactions/deposit")
  public ResponseEntity<TransactionDto> deposit(@RequestBody TransactionDto dto){
    TransactionDto newDeposit = transactionService.createNewDeposit(dto);
    return ResponseEntity.ok().body(dto);
  }

}
