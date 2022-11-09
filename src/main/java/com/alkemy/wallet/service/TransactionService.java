package com.alkemy.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final IAccountService accountService;

    public String moneySendInPesos(Long idTargetUser, Double amount, String type, String token) {
        return accountService.sendMoney(idTargetUser, amount, "peso Argentino(ARS)", 1, type, token);
    }

    public String moneySendInUsd(Long idTargetUser, Double amount, String type, String token) {
        return accountService.sendMoney(idTargetUser, amount, "dolar Estadounidense(USD)", 2, type, token);
    }
}
