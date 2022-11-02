package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;

import java.util.List;

public interface TransactionService {

    //Get One.
    TransactionDTO getById(Integer id);

    //Get All by User in a List.
    List<TransactionDTO> getAllByUser(Integer id);

    //Create
    void createTransaction(TransactionCreateDTO transDTO);

    //Update
    void updateTransaction(TransactionUpdateDTO transDTO, Integer id);
}
