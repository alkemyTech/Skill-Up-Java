package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.list.TransactionListResponseDto;

public interface ITransactionService {

    public TransactionListResponseDto listTransactions(Long userId);

}
