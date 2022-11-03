package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Transaction;

public interface ITransactionService {
    public void saveTransaction(Transaction transaction);
}
