package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.model.Transaction;

public interface TransactionService {

    Transaction createDeposit(Transaction transaction);
}
