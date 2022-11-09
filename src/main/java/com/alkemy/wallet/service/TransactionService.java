package com.alkemy.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final IAccountService accountService;

    public String moneySendInPesos(Long idUser, Long idTargetUser, Double amount, String type) {
        return accountService.sendMoney(idUser, idTargetUser, amount, "peso Argentino(ARS)", 1, type);
    }

    public String moneySendInUsd(Long idUser, Long idTargetUser, Double amount, String type) {
        return accountService.sendMoney(idUser, idTargetUser, amount, "dolar Estadounidense(USD)", 2, type);
    }
}
