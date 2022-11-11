package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface ITransactionService {
     ResponseEntity<Object> saveDeposit(TransactionDTO transaction);
     ResponseEntity<Object> savePayment(TransactionDTO transaction);
     ResponseEntity<Object> sendArs(TransactionDTO transaction);
     ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(Long userId);
     ResponseEntity<Object> sendUsd(TransactionDTO transaction);
     ResponseEntity<Object>updateTransaction(Long id, TransactionDTO transactionDTO);
     ResponseEntity<Page<TransactionEntity>> showTransactionPage(PageRequest pageRequest);
     void addNavigationAttributesToModel(int pageNumber, Model model, PageRequest pageRequest);
}
