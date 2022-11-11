package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Transaction;

import java.util.Optional;

public interface ITransactionService {
    Transaction saveTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(Long id);
    void deleteTransaction(Long id);
}
