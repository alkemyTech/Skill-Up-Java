package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.service.TransactionService;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final TransactionService service;
    //TODO use the interface, not the implementation
    private final AccountServiceImpl accountBalanceService;

    //TODO use the @RequiredArgsConstructor from lombok to dependency injection and remove the constructor
    public TransactionController(TransactionService service, AccountServiceImpl accountBalanceService) {
        this.service = service;
        this.accountBalanceService = accountBalanceService;
    }

    @GetMapping
    @PostMapping("/transactions/sendArs")
    public String moneySendInPesos(@RequestParam("idUser") Long idUser,
                                   @RequestParam("idTargetUser") Long idTargetUser,
                                   @RequestParam("mount") Double mount) {
        return service.moneySendInPesos(idUser, idTargetUser, mount);
    }

    @PostMapping("/transactions/sendUsd")
    public String moneySendInUsd(@RequestParam("idUser") Long idUser,
                                 @RequestParam("idTargetUser") Long idTargetUser,
                                 @RequestParam("mount") Double mount) {
        return service.moneySendInUsd(idUser, idTargetUser, mount);

    }

    @GetMapping("/account/balance/{idUser}")
    public ResponseEntity<AccountBalanceDto> getAccountBalance(@PathVariable("idUser") Long idUser) {
        return new ResponseEntity<>(accountBalanceService.getAccountBalance(idUser), HttpStatus.OK);


    }
}
