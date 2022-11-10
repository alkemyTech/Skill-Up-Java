package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.IBalance;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TransactionLimitEnum;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if(transaction.getAmount() <= 0){
            return new ResponseEntity<>("amount invalid", HttpStatus.FORBIDDEN);
        }
        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        account.setBalance(account.getBalance() + transaction.getAmount());
        return saveTransaction(transaction, DEPOSIT, account);
    }

    @Override
    public ResponseEntity<Object> savePayment(TransactionDTO transaction) {
        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        if(!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency()) ) {
            throw  new BankException("Currency not permitted");
        }
        if(!transaction.getType().equalsIgnoreCase(PAYMENT.getType())) {
            throw new BankException("Incorrect operation, only can be a payment");
        }
        if(transaction.getAmount() <= 0){
            return new ResponseEntity<>("amount invalid", HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() <= 0 || !(account.getBalance() >= transaction.getAmount())){
            return new ResponseEntity<>("insufficient funds", HttpStatus.FORBIDDEN);
        }
        account.setBalance(account.getBalance() - transaction.getAmount());
        return saveTransaction(transaction,PAYMENT, account);
    }

    @Override
    public ResponseEntity<Object> sendArs(TransactionDTO transaction) {
        if(!transaction.getType().equalsIgnoreCase(PAYMENT.getType())) {
            throw new BankException("Incorrect operation, only can be a payment");
        }
        if(transaction.getAmount() <= 0){
            return new ResponseEntity<>("amount invalid", HttpStatus.FORBIDDEN);
        }
        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());

        if(destinationAccount.isPresent()){
            if(destinationAccount.get().getCurrency().equals(ARS.getCurrency())){
                AccountEntity originAccount = bankDAO.getAccount(user.getUserId(), ARS.getCurrency());
                destinationAccount.get().setBalance(destinationAccount.get().getBalance() + transaction.getAmount());
                originAccount.setBalance(originAccount.getBalance() - transaction.getAmount());
                saveTransaction(transaction, PAYMENT, originAccount);
                return saveTransaction(transaction, INCOME, destinationAccount.orElseThrow(() -> new BankException("Destination account does not exist")));
            }
        }
        return new ResponseEntity<>("Send Invalid", HttpStatus.FORBIDDEN);
    }
    @Override
    public ResponseEntity<Object> sendUsd(TransactionDTO transaction) {
        if(!transaction.getType().equalsIgnoreCase(PAYMENT.getType())) {
            throw new BankException("Incorrect operation, only can be a payment");
        }
        if(transaction.getAmount() <= 0){
            return new ResponseEntity<>("amount invalid", HttpStatus.FORBIDDEN);
        }
        UserEntity user = bankDAO.findUserByEmail(returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());

        if(destinationAccount.isPresent()){
            if(destinationAccount.get().getCurrency().equals(USD.getCurrency())){
                AccountEntity originAccount = bankDAO.getAccount(user.getUserId(), USD.getCurrency());
                destinationAccount.get().setBalance(destinationAccount.get().getBalance() + transaction.getAmount());
                originAccount.setBalance(originAccount.getBalance() - transaction.getAmount());
                saveTransaction(transaction, PAYMENT, originAccount);
                return saveTransaction(transaction, INCOME, destinationAccount.orElseThrow(() -> new BankException("Destination account does not exist")));
            }
        }
        return new ResponseEntity<>("Send Invalid", HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<Object> updateTransaction(Long id, TransactionDTO transactionDTO) {
        bankDAO.updateTransaction(id, transactionDTO);
        return new ResponseEntity<>("Update transaction",HttpStatus.NO_CONTENT);
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

    public String returnUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
