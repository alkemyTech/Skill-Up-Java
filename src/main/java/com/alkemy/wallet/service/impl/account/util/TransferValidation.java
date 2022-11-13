package com.alkemy.wallet.service.impl.account.util;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.model.Account;

import java.util.Optional;

/**
 *  The transferValidation class contains the "checkTransaction"
 *  method used to check that the user to be transferred exists,
 *  that his balance is not less than the money to be sent,
 *  that the user does not send money to himself and that the money
 *  to be sent matches the type of account that will receive it.
 * @Author Guido Molina
 */
public class TransferValidation {

   public static void checkTransaction(TransactionCreateDTO transactionDTO,
                                       Account receiverAccount,
                                      Account senderAccount) {
        if (senderAccount.getId().equals(receiverAccount.getId())) {
            throw new TransactionException(ErrorList.REQUEST_FAILED.getMessage());
        }
        if(receiverAccount == null){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        if(!receiverAccount.getCurrency().equals(CurrencyList.USD)){
            throw new TransactionException(ErrorList.ACCOUNTS_DIFERENT_CURRENCY.getMessage());
        }
       if(senderAccount.getBalance() < transactionDTO.getAmount()){
          throw new TransactionException(ErrorList.INSUFFICIENT_BALANCE.getMessage());
       }
        if(transactionDTO.getAmount() > senderAccount.getTransactionLimit()){
            throw new TransactionException(ErrorList.TRANSACTION_LIMIT.getMessage());
        }
    }
}
