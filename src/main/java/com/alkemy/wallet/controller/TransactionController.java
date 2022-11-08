package com.alkemy.wallet.controller;

import com.alkemy.wallet.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
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
}
