package com.alkemy.wallet.controller;

import com.alkemy.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/sendArs")
    public ResponseEntity<String> moneySendInPesos(@RequestParam("idUser") Long idUser, @RequestParam("idTargetUser") Long idTargetUser,
                                                   @RequestParam("mount") Double mount, @RequestParam("type") String type) {
        return new ResponseEntity<>(service.moneySendInPesos(idUser, idTargetUser, mount, type), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<String> moneySendInUsd(@RequestParam("idUser") Long idUser, @RequestParam("idTargetUser") Long idTargetUser,
                                                 @RequestParam("mount") Double mount, @RequestParam("type") String type) {
        return new ResponseEntity<>(service.moneySendInUsd(idUser, idTargetUser, mount, type), HttpStatus.ACCEPTED);

    }
}
