package com.alkemy.wallet.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        List<Transaction> allTransactions = repository.findAll();
        List<Transaction> accountTransactions = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getAccount().getAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }

        return accountTransactions;
    }

}
