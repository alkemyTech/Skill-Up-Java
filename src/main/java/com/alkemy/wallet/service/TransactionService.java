package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.exception.AccountLimitException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
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

    JwtUtil jwtUtil;


    @Override
    public HashSet<TransactionDto> getByUserId(@Valid List<AccountDto> accounts) {

        List<Long> accounts_id = new ArrayList<>();
        for (AccountDto accountDto : accounts) {
            accounts_id.add(mapper.getMapper().map(accountDto, Long.class));
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
            User senderUser = userRepository.findById(Long.parseLong(jwtUtil.getKey(token))).orElseThrow(() -> new ResourceNotFoundException("Usuario inexistente"));
            AccountDto senderAccount = accountService.getAccountByCurrency(senderUser.getId(), Currency.usd);
            accountService.checkAccountLimit(senderAccount, destinedTransactionDto);
            TransactionDto transactionPayment = createTransactions(new Transaction(destinedTransactionDto.getAmount(), TypeOfTransaction.income, destinedTransactionDto.getDescription(), destinedTransactionDto.getAccount()),
                    new Transaction(destinedTransactionDto.getAmount(), TypeOfTransaction.payment, destinedTransactionDto.getDescription(), mapper.getMapper().map(senderAccount, Account.class)));

            return ResponseEntity.status(HttpStatus.OK).body(mapper.getMapper().map(transactionPayment, TransactionDto.class));
        } catch (ResourceNotFoundException | UserNotLoggedException | AccountLimitException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @Override
    public Page<Transaction> paginateTransactionByUserId(Long id, int page, int size, String token) {

        UserDto user = userService.findByEmail(jwtUtil.getValue(token));

        Pageable pageable = PageRequest.of(page,size);

        Page<Transaction> pageTransactions = transactionRepository.findByAccount_User_Id(id, pageable);

        return pageTransactions;
    }

}