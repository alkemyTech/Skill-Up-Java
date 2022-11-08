package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.response.transaction.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.mapper.response.transaction.ITransactionResponseMapper;
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
    private ITransactionResponseMapper mapper;

    @Override
    public List<TransactionResponseDto> listTransactions(Long userId) {
        List<Transaction> transactionsByUserId = transactionRepository.getByUserId(userId);
        if(transactionsByUserId.isEmpty())
            return Collections.emptyList();

        return mapper.listTransactionToListTransactionRespose(transactionsByUserId);
    }
}
