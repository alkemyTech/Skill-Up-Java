package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.TransactionDto;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    ITransactionRepository repository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteTransaction(Long id) {
        repository.deleteById(id);
    }
}
