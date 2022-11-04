package com.alkemy.wallet.service.impl.transaction.strategy;

import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class DepositStrategy implements ITransactionStrategy{

    @Override
    public void make(double amount, Account account) {
        if (account.getTransactionLimit() < amount) {
            throw new TransactionException(ErrorList.TRANSACTION_LIMIT.getMessage());
        }else{
            account.setBalance(account.getBalance()+amount);
            account.setTransactionLimit(account.getTransactionLimit()-amount);
        }
    }
}
