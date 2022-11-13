package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface ITransactionService {

    // Get One.
    TransactionDTO getTransactionById(Integer id, Integer user_id);

    // Get All by User in a List.
    TransactionPageDTO findAllByUserId(Integer id, Integer page);

    // Create
    void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy);

    void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy, String accDestinyEmail);

    void makeTransaction(ITransactionStrategy strategy, Account account, Double amount);

    // Update
    void updateTransaction(TransactionUpdateDTO transDTO, Integer id, Integer user_id);

    TransactionResponseDTO sendMoney(TransactionCreateDTO transaction, String bearerToken, String currency)
            throws ParseException;
}
