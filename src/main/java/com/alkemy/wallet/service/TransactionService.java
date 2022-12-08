package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.exception.*;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ITransactionService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    Mapper mapper;

    @Autowired
    ITransactionRepository transactionRepository;

    @Autowired
    IUserService userService;

    @Autowired
    IUserRepository userRepository;


    @Autowired
    IAccountService accountService;

    @Autowired
    IAccountRepository accountRepository;

    @Autowired
    JwtUtil jwtUtil;


    @Override
    public HashSet<TransactionDto> getByUserId(@Valid List<Account> accounts) {

        List<Long> accounts_id = new ArrayList<>();
        for (Account account : accounts) {
            accounts_id.add(account.getId());
        }

        return transactionRepository.findByAccount_idIn(accounts_id).stream().map((transaction) ->
                        mapper.getMapper().map(transaction, TransactionDto.class))
                .collect(Collectors.toCollection(HashSet::new));

    }

    @Override
    public TransactionDto createTransactions(Transaction transactionIncome, Transaction transactionPayment) {
        transactionRepository.save(transactionIncome);
        return mapper.getMapper().map(transactionRepository.save(transactionPayment), TransactionDto.class);
    }

    @Override
    public ResponseEntity<Object> makeTransaction(String token, TransactionDto destinedTransactionDto) {
        try {
            userService.checkLoggedUser(token);
            User senderUser = userRepository.findByEmail(jwtUtil.getValue(token));
            Account senderAccount = accountService.getAccountByCurrency(senderUser.getId(), destinedTransactionDto.getAccount().getCurrency());
            Account destinedAccount = accountRepository.findById(destinedTransactionDto.getAccount().getId()).orElseThrow(() -> new ResourceNotFoundException("The receiving account does not exist"));
            accountService.checkAccountLimit(senderAccount, destinedTransactionDto);
            checkBalance(senderAccount.getBalance(), destinedTransactionDto.getAmount());
            TransactionDto transactionPayment = createTransactions(new Transaction(destinedTransactionDto.getAmount(), TypeOfTransaction.income, destinedTransactionDto.getDescription(), destinedAccount),
                    new Transaction(destinedTransactionDto.getAmount(), TypeOfTransaction.payment, destinedTransactionDto.getDescription(), mapper.getMapper().map(senderAccount, Account.class)));

            return ResponseEntity.status(HttpStatus.OK).body(mapper.getMapper().map(transactionPayment, TransactionDto.class));
        } catch (ResourceNotFoundException | UserNotLoggedException | AccountLimitException |
                 NotEnoughCashException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @Override
    public Page<Transaction> paginateTransactionByUserId(Long id, int page, int size, String token) {

        UserDto user = userService.findByEmail(jwtUtil.getValue(token));

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> pageTransactions = transactionRepository.findByAccount_User_Id(id, pageable);

        return pageTransactions;

    }

    public ResponseEntity<?> getTransaction(Long id, String token) {
        try {
            userService.checkLoggedUser(token);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.findById(id), TransactionDto.class));
        } catch (UserNotLoggedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
    }

    @Override
    public ResponseEntity<?> patchTransaction(Long id, String token, String description) {
        try {
            userService.checkLoggedUser(token);
            Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction doesn't exist"));
            transaction.setDescription(description);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.save(transaction), TransactionDto.class));
        } catch (UserNotLoggedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
    }

    @Override
    public boolean checkBalance(Double balance, Double amount) {
        if (balance < amount) {
            throw new NotEnoughCashException("Not enough cash");
        } else {
            return true;
        }
    }

    @Override
    public boolean checkTransactionAmount(Double amount) {
        if (amount < 0) {
            throw new NoAmountException("Cannot make a transaction without amount");
        } else return true;
    }

    @Override
    public ResponseEntity<?> createPayment(TransactionDto transactionDto) {
        try {
            Account account = accountRepository.findById(transactionDto.getAccount().getId()).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            Transaction transaction = transactionRepository.save(new Transaction(transactionDto.getAmount(), TypeOfTransaction.payment, transactionDto.getDescription(), account));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.getMapper().map(transaction, TransactionDto.class));
        } catch (NoAmountException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
    }

    @Override
    public ResponseEntity<?> createDeposit(TransactionDto transactionDto) {
        try {
            Account account = accountRepository.findById(transactionDto.getAccount().getId()).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            Transaction transaction = transactionRepository.save(new Transaction(transactionDto.getAmount(), TypeOfTransaction.deposit, transactionDto.getDescription(), account));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.getMapper().map(transaction, TransactionDto.class));
        } catch (NoAmountException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
    }

}