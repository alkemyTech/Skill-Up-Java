package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.TransactionEntity;

public interface ITransactionService {
    public void saveTransaction(TransactionEntity transaction);
}
