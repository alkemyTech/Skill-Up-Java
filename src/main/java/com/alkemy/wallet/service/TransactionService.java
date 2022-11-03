package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.model.Transaction;

public interface TransactionService {
    TransactionDetailDto getTransactionDetailById (Integer Id) throws Exception;

    Transaction createDeposit(Transaction transaction);

}
