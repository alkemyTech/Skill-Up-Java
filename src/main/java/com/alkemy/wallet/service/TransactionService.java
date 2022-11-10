package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final IAccountService accountService;
    private final ITransactionRepository transactionRepository;

    public String moneySendInPesos(Long idTargetUser, Double amount, String type, String token) {
        return accountService.sendMoney(idTargetUser, amount, "peso Argentino(ARS)", 1, type, token);
    }

    public String moneySendInUsd(Long idTargetUser, Double amount, String type, String token) {
        return accountService.sendMoney(idTargetUser, amount, "dolar Estadounidense(USD)", 2, type, token);
    }
    Transaction save(Transaction entity){
       return transactionRepository.save(entity);
    }
}
