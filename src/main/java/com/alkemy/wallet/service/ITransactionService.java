package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.http.ResponseEntity;

public interface ITransactionService {
    public void saveTransaction(TransactionDTO transaction);


    void saveTransaction(Transaction transaction);

    ResponseEntity<TransactionDTO> getTransferArsById(Long transactionId);

    Object getAllTransactionByUser(Long id);

    Object createTransactionArs(TransactionDTO transactionDTO);
}
