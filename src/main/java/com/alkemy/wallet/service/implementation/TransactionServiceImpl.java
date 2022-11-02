package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;


    @Override
    public Transaction createDeposit(Transaction transaction) {
        Double amount = transaction.getAmount();

        // It would be nice to have an exception handler. We should implement it in a separate branch
        if(amount <= 0) {
            throw new IllegalArgumentException("The amount must be greater than 0");
        }

        return transactionRepository.save(transaction);
    }
}
