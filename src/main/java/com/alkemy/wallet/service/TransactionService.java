package com.alkemy.wallet.service;

import java.util.List;
import com.alkemy.wallet.model.entity.Transaction;

public interface TransactionService {
    public List<Transaction> findAllByAccountId(Long accountId);
}
