package com.alkemy.wallet.service.impl.transaction.strategy;

import com.alkemy.wallet.model.Account;

public interface ITransactionStrategy {

    void make(double amount, Account account);
}
