package com.alkemy.wallet.service.impl.transaction.util;

import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.enumeration.TypeList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.model.Account;

public class InvestStrategy implements ITransactionStrategy{

    @Override
    public TypeList type() {
        return TypeList.INVEST;
    }

    @Override
    public void make(double amount, Account accOrigin) {
        this.modifyAccountTransactionLimit(amount, accOrigin);
        if(accOrigin.getBalance() < amount){
            throw new TransactionException(ErrorList.INSUFFICIENT_BALANCE.getMessage());
        }else{
            accOrigin.setBalance(accOrigin.getBalance()-amount);
        }
    }
}
