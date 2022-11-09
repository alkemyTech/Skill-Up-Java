package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.IBalance;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;
import static com.alkemy.wallet.model.TypeEnum.*;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements ITransactionService {

    private final BankDAO bankDAO;

    @Override
    public ResponseEntity<Object> saveDeposit(TransactionDTO transaction) {
        if(!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency()) ) {
            throw  new BankException("Currency not permitted");
        }
        if(!transaction.getType().equalsIgnoreCase(DEPOSIT.getType())) {
            throw new BankException("Incorrect operation, only can be a deposit");
        }
        AccountEntity account = bankDAO.getAccount(1L, transaction.getCurrency().toUpperCase());
        return saveTransaction(transaction, DEPOSIT, account);
    }

    @Override
    public ResponseEntity<Object> savePayment(TransactionDTO transaction) {
        if(!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency()) ) {
            throw  new BankException("Currency not permitted");
        }
        if(!transaction.getType().equalsIgnoreCase(PAYMENT.getType())) {
            throw new BankException("Incorrect operation, only can be a payment");
        }
        AccountEntity account = bankDAO.getAccount(1L, transaction.getCurrency().toUpperCase());
        return saveTransaction(transaction, PAYMENT, account);
    }

    @Override
    public ResponseEntity<Object> sendArs(TransactionDTO transaction) {
        if(!transaction.getType().equalsIgnoreCase(INCOME.getType())) {
            throw new BankException("Incorrect operation, only can be an income");
        }

        List<IBalance> transactionEntity = bankDAO.getBalance();
        System.out.println("*************** " + transactionEntity.get(0).getSumIncome());

        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());
        //Falta obtener userId del usuario autenticado
        Optional<AccountEntity> sourceAccount = bankDAO.getAccountById(1L);
        saveTransaction(transaction, PAYMENT , sourceAccount.orElseThrow(() -> new BankException("Source account does not exist")));
        return saveTransaction(transaction, INCOME, destinationAccount.orElseThrow(() -> new BankException("Destination account does not exist")));
    }

    @Override
    public ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(Long userId) {
        Optional<UserEntity> opUser = bankDAO.getUserById(userId);

        if(opUser.isEmpty()) {
            throw new BankException("The requested user ID does not exist");
        }

        List<AccountEntity> accounts = bankDAO.getAllAccountByUser(opUser.get());
        List<TransactionEntity> transactions = new ArrayList<>();
        for(AccountEntity account : accounts) {
            transactions.addAll(bankDAO.getAllTransactionByAccount(account));
        }

        return ResponseEntity.ok(transactions);
    }

    private ResponseEntity<Object> saveTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        bankDAO.createTransaction(transaction, type, accountEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
