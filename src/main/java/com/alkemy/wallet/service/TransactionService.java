package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.model.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionDetailDto getTransactionDetailById (Integer Id) throws Exception;
    List<TransactionDetailDto> getTransactionsDetailByAccountId (Integer accountId);

    Transaction createDeposit(Transaction transaction);

}
