package com.alkemy.wallet.service.impl.transaction.util;

import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.enumeration.TypeList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.model.Account;

public interface ITransactionStrategy {

    //Type is one of the following strategies : DEPOSIT, PAYMENT, INCOME
    TypeList type();
    void make(double amount, Account account);
    default void modifyAccountTransactionLimit(double amount, Account account){
        if(account.getTransactionLimit()<amount) {
            throw new TransactionException(ErrorList.TRANSACTION_LIMIT.getMessage());
        }
        account.setTransactionLimit(account.getTransactionLimit()-amount);
    }/*
    void make(double amount, Account accOrigin, Account accDestiny);*/
}
