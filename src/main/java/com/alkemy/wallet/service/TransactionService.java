package com.alkemy.wallet.service;

import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final ITransactionRepository repository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
    private final SendMoneyImpl sendMoney;

    public TransactionService(ITransactionRepository repository, IUserRepository userRepository, IAccountRepository accountRepository, SendMoneyImpl sendMoney) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.sendMoney = sendMoney;
    }

    public String moneySendInPesos(Long idUser, Long idTargetUser, Double amount) {
        return sendMoney.sendMoney(idUser, idTargetUser, amount, "peso Argentino(ARS)", 1, repository, userRepository, accountRepository);
    }

    public String moneySendInUsd(Long idUser, Long idTargetUser, Double amount) {
        return sendMoney.sendMoney(idUser, idTargetUser, amount, "dolar Estadounidense(USD)", 2, repository, userRepository, accountRepository);
    }
}
