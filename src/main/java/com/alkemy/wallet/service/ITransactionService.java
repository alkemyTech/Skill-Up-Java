package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.transaction.TransactionResponseDto;

import java.util.List;

public interface ITransactionService {

    public List<TransactionResponseDto> listTransactions(Long userId);

}
