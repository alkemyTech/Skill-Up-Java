package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    UserEntity user;
    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();

    @PostMapping("/deposit")
    public ResponseEntity<Object> deposit(@RequestBody TransactionDTO transactionDTO) {
        transactionService.saveDeposit(transactionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
