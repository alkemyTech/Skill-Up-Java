package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.IBalance;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
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

        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        return saveTransaction(transaction,PAYMENT, account);
    }

    @Override
    public ResponseEntity<Object> sendArs(TransactionDTO transaction) {
        if(!transaction.getType().equalsIgnoreCase(INCOME.getType())) {
            throw new BankException("Incorrect operation, only can be an income");
        }
        List<IBalance> transactionEntity = bankDAO.getBalance();
        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());
        AccountEntity sourceAccount = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        saveTransaction(transaction, PAYMENT, sourceAccount);
        return saveTransaction(transaction, INCOME, destinationAccount.orElseThrow(() -> new BankException("Destination account does not exist")));
    }

    private ResponseEntity<Object> saveTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        bankDAO.createTransaction(transaction, type, accountEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    public String returnUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
