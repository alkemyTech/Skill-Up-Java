package com.alkemy.wallet.service.impl.transaction.util;

import com.alkemy.wallet.enumeration.TypeList;
import com.alkemy.wallet.model.Account;

public class DepositStrategy implements ITransactionStrategy {

    @Override
    public TypeList type() {
        return TypeList.DEPOSIT;
    }

    @Override
    public void make(double amount, Account account) {
        this.checkAccountTransactionLimit(amount, account);
        account.setBalance(account.getBalance() + amount);

    }
}
