package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateDescription(Long transactionId, String description, Long accountId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);

        if (transaction != null && transaction.getAccount().getAccountId() == accountId) {
            transaction.setDescription(description);
            this.saveTransaction(transaction);
            return transaction;
        }

        return null;
    }
}
