package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;

import java.util.List;

public interface ITransactionService {

    //Get One.
    TransactionDTO getTransactionById(Integer id);

    //Get All by User in a List.
    List<TransactionDTO> getAllByUserId(Integer id);

    //Create
    void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy);
    void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy,String accDestinyEmail);
    void makeTransaction(ITransactionStrategy strategy, Account account, Double amount);

    //Update
    void updateTransaction(TransactionUpdateDTO transDTO, Integer id);
}
