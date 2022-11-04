package com.alkemy.wallet.service.impl.transaction.strategy;

import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.model.Account;

public class PaymentStrategy implements ITransactionStrategy{


    @Override
    public void make(double amount, Account account) {
        if(account.getBalance() < amount){
            throw new TransactionException(ErrorList.INSUFFICIENT_BALANCE.getMessage());
        }else if(account.getTransactionLimit()<amount){
            throw new TransactionException(ErrorList.TRANSACTION_LIMIT.getMessage());
        }else{
            account.setBalance(account.getBalance()-amount);
            account.setTransactionLimit(account.getTransactionLimit()-amount);
        }
    }
}
