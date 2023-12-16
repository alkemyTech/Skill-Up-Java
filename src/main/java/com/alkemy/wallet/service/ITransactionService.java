package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.data.domain.Page;

public interface ITransactionService {

    TransactionResponseDto sendMoneyIndicatingCurrency(String currencyType, TransactionRequestDto transactionRequestDto);

    TransactionResponseDto updateTransaction(Long id, UpdateTransactionRequestDto transactionRequestDto);

    TransactionResponseDto getTransactionDetails (Long id);

    Transaction getTransactionById(Long id);

    TransactionResponseDto doPayment(TransactionRequestDto transactionRequestDto);

    TransactionResponseDto doDeposit(TransactionRequestDto transactionRequestDto);

    Page<TransactionResponseDto> getAllTransactions(Long userId, Integer pageNumber);
}