package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.response.list.TransactionListResponseDto;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.mapper.TransactionMapper;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper mapper;

    @Override
    public TransactionListResponseDto listTransactions(Long userId) {
        TransactionListResponseDto transactionsByUserId = mapper.entityList2DtoList(transactionRepository.getByUserId(userId));
        if(transactionsByUserId.getTransactions().isEmpty())
            throw new IllegalArgumentException(String.format("List is empty or null: %s", transactionsByUserId.getTransactions()));;

        return transactionsByUserId;
    }
}
