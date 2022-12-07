package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

public interface ITransactionService {
    HashSet<TransactionDto> getByUserId(@Valid List<Account> accounts);

    TransactionDto createTransactions(Transaction transactionIncome, Transaction transactionPayment);

    ResponseEntity<Object> makeTransaction(String token, TransactionDto destinedTransactionDto);

    ResponseEntity<?> getTransaction(Long id, String token);

    ResponseEntity<?>  patchTransaction(Long id, String token, String description);


    boolean checkBalance(Double balance, Double amount);
}
