package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.TransactionEntity;

public interface ITransactionService {
    public void saveTransaction(TransactionEntity transaction);
    public void saveDeposit(TransactionDTO transaction);
}
