package com.alkemy.wallet.service;

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
