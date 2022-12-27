package com.alkemy.wallet.utils;

import com.alkemy.wallet.model.constant.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;

public class TransactionUtil {

    public static final int MIN_TRANSACTION_LIMIT = 1000;

    public static final int MIN_AMOUNT_TO_SEND = 100;

    public static Transaction setTransactionValues(Double amount, TransactionTypeEnum transactionType,
                                                   String description, User user, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(transactionType);
        transaction.setDescription(description);
        transaction.setUser(user);
        transaction.setAccount(account);

        return transaction;
    }
}
