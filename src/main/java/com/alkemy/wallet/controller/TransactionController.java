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
    public ResponseEntity<String> moneySendInPesos(@RequestParam("idTargetUser") Long idTargetUser, @RequestParam("mount") Double mount,
                                                   @RequestParam("type") String type, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.moneySendInPesos(idTargetUser, mount, type, token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<String> moneySendInUsd(@RequestParam("idTargetUser") Long idTargetUser, @RequestParam("mount") Double mount,
                                                 @RequestParam("type") String type, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.moneySendInUsd(idTargetUser, mount, type, token), HttpStatus.ACCEPTED);

    }
}
