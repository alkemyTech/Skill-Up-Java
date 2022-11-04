package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionEntity transaction){
        transactionRepository.save(transaction);
    }
}
