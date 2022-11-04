package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.model.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionDetailDto getTransactionDetailById (Integer Id) throws Exception;

    TransactionDepositDto createDeposit(TransactionDepositDto transaction);

    List<TransactionDetailDto> getTransactions(Integer userId);
}
