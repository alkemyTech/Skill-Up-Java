package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.TransactionDto;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    ITransactionRepository repository;

    @Override
    public TransactionDto saveTransaction(TransactionDto transactionDto) {
        ModelMapper mapper = new ModelMapper();
        Transaction transaction = repository.save(mapper.map(transactionDto, Transaction.class));

        return mapper.map(transaction, TransactionDto.class);
    }
}
