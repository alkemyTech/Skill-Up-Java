package com.alkemy.wallet.service.impl.transaction.util;

import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.enumeration.TypeList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.model.Account;

public class IncomeStrategy implements ITransactionStrategy {
    @Override
    public TypeList type() {
        return TypeList.INCOME;
    }

    @Override
    public void make(double amount, Account account) {
        account.setBalance(account.getBalance() + amount);
    }
}
