package com.alkemy.wallet.utils;

import com.alkemy.wallet.model.constant.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;

import java.time.LocalDateTime;

public class TransactionUtil {

    public static final int MIN_TRANSACTION_LIMIT = 1000;

    public static final int MIN_AMOUNT_TO_SEND = 100;

    public static Transaction setTransactionValues(Double amount, TransactionTypeEnum transactionType,
                                    String description, User user, Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(transactionType)
                .description(description)
                .transactionDate(LocalDateTime.now())
                .user(user)
                .account(account)
                .build();
    }
}
