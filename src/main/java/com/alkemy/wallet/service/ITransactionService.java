package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITransactionService {

    TransactionResponseDto sendMoneyIndicatingCurrency(String currencyType, TransactionRequestDto request);

    TransactionResponseDto update(Long id, UpdateTransactionRequestDto request);

    TransactionResponseDto getDetails (Long id);

    Transaction getById(Long id);

    TransactionResponseDto payment(TransactionRequestDto request);

    TransactionResponseDto deposit(TransactionRequestDto request);

    List<TransactionResponseDto> listTransactionsByUserId(Long userId);

    Page<TransactionResponseDto> getAll(Integer pageNumber);
}