package com.alkemy.wallet.service;

import com.alkemy.wallet.model.TransactionDto;
import com.alkemy.wallet.model.response.TransactionResponseDto;

public interface ITransactionService {
    TransactionDto saveTransaction(TransactionDto transactionResponseDto);
}
