package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

public interface ITransactionService {
    HashSet<TransactionDto> getByUserId(@Valid List<Account> accounts);

    TransactionDto createTransactions(Transaction transactionIncome, Transaction transactionPayment);

    ResponseEntity<Object> makeTransaction(String token, TransactionDto destinedTransactionDto);

    Page<TransactionDto> paginateTransactionByUserId(Long id, int page, int size, String token);

    ResponseEntity<?> getTransaction(Long id, String token);

    ResponseEntity<?>  patchTransaction(Long id, String token, String description);

    boolean checkBalance(Double balance, Double amount);
}
