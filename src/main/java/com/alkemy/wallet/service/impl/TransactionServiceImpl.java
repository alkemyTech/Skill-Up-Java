package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;
import static com.alkemy.wallet.model.TypeEnum.*;
import static com.alkemy.wallet.model.TypeEnum.DEPOSIT;

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
        TypeEnum type = DEPOSIT;
        return saveTransaction(transaction, type, account);
    }

    @Override
    public ResponseEntity<Object> savePayment(TransactionDTO transaction) {
        if(!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency()) ) {
            throw  new BankException("Currency not permitted");
        }
        if(!transaction.getType().equalsIgnoreCase(PAYMENT.getType())) {
            throw new BankException("Incorrect operation, only can be a payment");
        }
        TypeEnum type = PAYMENT;
        AccountEntity account = bankDAO.getAccount(1L, transaction.getCurrency().toUpperCase());
        return saveTransaction(transaction, type, account);
    }

    @Override
    public ResponseEntity<Object> sendArs(TransactionDTO transaction) {
        if(!transaction.getType().equalsIgnoreCase(INCOME.getType())) {
            throw new BankException("Incorrect operation, only can be an income");
        }
        TypeEnum destionationType = INCOME;
        TypeEnum sourceType = PAYMENT;
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());
        //Falta obtener userId del usuario autenticado
        Optional<AccountEntity> sourceAccount = bankDAO.getAccountById(1L);
        saveTransaction(transaction, sourceType , sourceAccount.orElseThrow(() -> new BankException("The account does not exist")));
        return saveTransaction(transaction, destionationType, destinationAccount.orElseThrow(() -> new BankException("The account does not exist")));
    }

    private ResponseEntity<Object> saveTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        bankDAO.createTransaction(transaction, type, accountEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
