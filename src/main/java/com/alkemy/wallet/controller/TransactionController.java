package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
/*
    @Autowired
    private AccountEntity account;

    @Autowired
    private TransactionRepository transactionRepository;

    UserEntity user;
    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amount")Double amount,
                          @RequestParam("accountId") Long accountId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes){

        // CHECK FOR EMPTY STRINGS:
        if(amount == 0 || accountId == 0){
            redirectAttributes.addFlashAttribute("Deposit Amount or Account Depositing to Can't Be 0!");
            return "redirect:/";
        }
        // GET LOGGED IN USER:
        user = (UserEntity) session.getAttribute("user");

        // CHECK IF DEPOSIT AMOUNT IS 0:
        if(amount == 0){
            redirectAttributes.addFlashAttribute("Deposit Amount Can't Be 0!");
            return "redirect:/";
        }

        // UPDATE BALANCE:
        currentBalance = account.getBalance();

        newBalance = currentBalance + amount;

        // Log Successful Transaction
        transactionRepository.save(null);
        redirectAttributes.addFlashAttribute("success", "Amount Deposited Successfully!");
        return "redirect:/";
    }
// End Of Deposits.*/
}
