package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Transaction;

import java.util.List;

public interface ITransactionService {

    TransactionResponseDto sendMoneyIndicatingCurrency(String currencyType, TransactionRequestDto request, String token);

    TransactionResponseDto update(Long id, UpdateTransactionRequestDto request, String token);

    TransactionResponseDto getDetails (Long id, String token);

    Transaction getById(Long id);

    TransactionResponseDto payment(TransactionRequestDto request, String token);

    TransactionResponseDto deposit(TransactionRequestDto request, String token);

    List<TransactionResponseDto> listTransactionsByUserId(Long userId);
}