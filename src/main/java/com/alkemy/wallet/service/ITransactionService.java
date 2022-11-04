package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;

import java.util.List;

public interface ITransactionService {

    //Get One.
    TransactionDTO getTransactionById(Integer id);

    //Get All by User in a List.
    List<TransactionDTO> getAllByUserId(Integer id);

    //Create
    void makeTransaction(TransactionCreateDTO transDTO);

    //Update
    void updateTransaction(TransactionUpdateDTO transDTO, Integer id);
}
