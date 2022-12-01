package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransactionService {
    @Autowired
    Mapper mapper;

    @Autowired
    ITransactionRepository transactionRepository;

    //    public TransactionDto save(@Valid TransactionDto transactionDto) {
//        Transaction transaction = mapper.getMapper().map(transactionDto, Transaction.class);
//        transactionRepository.save(transaction);
//        return transactionDto;
//    }
    public HashSet<TransactionDto> getByUserId(@Valid Long id) {
        HashSet<Transaction> transactions = transactionRepository.findByUserId(id);
        HashSet<TransactionDto> transactionDtos = new HashSet<>();
        transactions.stream().map((transaction) -> transactionDtos.add(mapper.getMapper().map(transaction, TransactionDto.class)));
        return transactionDtos;
    }
}
