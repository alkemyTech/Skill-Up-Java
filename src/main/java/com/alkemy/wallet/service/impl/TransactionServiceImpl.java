package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDTO transaction) {

    }

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }



    @Override
    public ResponseEntity<TransactionDTO> getTransferArsById(Long transactionId) {
        return null;
    }

    @Override
    public Object getAllTransactionByUser(Long id) {
        return null;
    }

    @Override
    public Object createTransactionArs(TransactionDTO transactionDTO) {
        return null;
    }
}
