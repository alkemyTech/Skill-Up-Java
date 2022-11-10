package com.alkemy.wallet;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDepositRequestDto;
import com.alkemy.wallet.exception.ForbiddenAccessException;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.TransactionLimitExceededException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.TransactionService;
import com.alkemy.wallet.service.UserService;
import com.alkemy.wallet.service.implementation.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CreateDepositTests {

    @Mock
    AccountService accountService;

    @Mock
    UserService userService;

    @Mock
    JWTUtil jwtUtil;

    @Mock
    TransactionMapper transactionMapper;

    @Mock
    AccountMapper accountMapper;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService =  new TransactionServiceImpl();

    String dummyToken;

    @BeforeEach
    void setUp(){
        dummyToken = "tokenDummy";
    }
    // Exceptions

    @Test
    void userDoesntOwnAccountTest(){
        AccountDto accountDto = new AccountDto(1, 1, 2000.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);
        User user = new User(4);
        Mockito.when(accountService.getAccountById(1)).thenReturn(accountDto);
        Mockito.when(userService.loadUserByUsername(any())).thenReturn(user);
        TransactionDepositRequestDto transactionDepositRequestDto = new TransactionDepositRequestDto(2000.0, "new transaction", 1);

        Assertions.assertThrows(ForbiddenAccessException.class, () -> transactionService.createDeposit(transactionDepositRequestDto, dummyToken));
    }

    @Test
    void invalidAmountTest(){
        AccountDto accountDto = new AccountDto(1, 1, 2000.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);

        Mockito.when(accountService.getAccountById(1)).thenReturn(accountDto);
        Mockito.when(userService.loadUserByUsername(any())).thenReturn(new User());
        Mockito.when(accountService.hasUserAccountById(any(),any())).thenReturn(true);
        TransactionDepositRequestDto transactionDepositRequestDto = new TransactionDepositRequestDto(0.0, "new transaction", 1);

        Assertions.assertThrows(InvalidAmountException.class, () -> transactionService.createDeposit(transactionDepositRequestDto, dummyToken));
    }

    @Test
    void amountExceedsTransactionLimitTest(){
        AccountDto accountDto = new AccountDto(1, 1, 4000.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);

        Mockito.when(accountService.getAccountById(1)).thenReturn(accountDto);
        Mockito.when(userService.loadUserByUsername(any())).thenReturn(new User());
        Mockito.when(accountService.hasUserAccountById(any(),any())).thenReturn(true);
        Mockito.when(accountService.increaseBalance(1, 6001.0)).thenReturn(accountDto);
        TransactionDepositRequestDto transactionDepositRequestDto = new TransactionDepositRequestDto(6001.0, "new transaction", 1);

        Assertions.assertThrows(TransactionLimitExceededException.class, () -> transactionService.createDeposit(transactionDepositRequestDto, dummyToken));
    }

    @Test
    void accountDoesntExist(){
        Mockito.when(accountService.getAccountById(any())).thenThrow(new ResourceNotFoundException(""));
        TransactionDepositRequestDto transactionDepositRequestDto = new TransactionDepositRequestDto(2500.0, "new transaction", 1);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> transactionService.createDeposit(transactionDepositRequestDto, dummyToken));
    }
    // Happy path
    @Test
    void createDepositTest(){
        TransactionDepositDto expectedDeposit = new TransactionDepositDto(3000.0, "This is a deposit");
        TransactionDepositRequestDto depositRequest = new TransactionDepositRequestDto(3000.0, "This is a deposit", 8);
        TransactionDepositDto actualDeposit = new TransactionDepositDto(depositRequest.getAmount(), depositRequest.getDescription());

        AccountDto accountDtoBeforeIncrease = new AccountDto(1, 1, 1500.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);
        AccountDto accountDtoAfterIncrease  = new AccountDto(1, 1, 4500.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);;
        actualDeposit.setAccount(accountMapper.convertToEntity(accountDtoAfterIncrease));
        Transaction newTransaction = transactionMapper.convertToEntity(actualDeposit);

        Mockito.when(accountService.getAccountById(8)).thenReturn(accountDtoBeforeIncrease);
        Mockito.when(userService.loadUserByUsername(any())).thenReturn(new User(1));
        Mockito.when(accountService.hasUserAccountById(any(),any())).thenReturn(true);
        Mockito.when(accountService.increaseBalance(1, 3000.0)).thenReturn(accountDtoAfterIncrease);
        Mockito.when(transactionRepository.save(transactionMapper.convertToEntity(actualDeposit))).thenReturn(newTransaction);
        Mockito.when(transactionMapper.convertToTransactionDepositDto(newTransaction)).thenReturn(actualDeposit);

        Assertions.assertEquals(expectedDeposit, transactionService.createDeposit(depositRequest, dummyToken));
    }


    // Border values

    @Test
    void createDepositWithAmountNearTransactionLimitTest(){
        TransactionDepositDto expected = new TransactionDepositDto(5000.0, "This is a deposit");
        TransactionDepositRequestDto depositRequest = new TransactionDepositRequestDto(5000.0, "This is a deposit", 8);
        TransactionDepositDto actual = new TransactionDepositDto(depositRequest.getAmount(), depositRequest.getDescription());

        AccountDto accountDtoBeforeIncrease = new AccountDto(1, 1, 1500.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);
        AccountDto accountDtoAfterIncrease  = new AccountDto(1, 1, 6500.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);;
        actual.setAccount(accountMapper.convertToEntity(accountDtoAfterIncrease));
        Transaction newTransaction = transactionMapper.convertToEntity(actual);

        Mockito.when(accountService.getAccountById(8)).thenReturn(accountDtoBeforeIncrease);
        Mockito.when(userService.loadUserByUsername(any())).thenReturn(new User(1));
        Mockito.when(accountService.hasUserAccountById(any(),any())).thenReturn(true);
        Mockito.when(accountService.increaseBalance(1, 5000.0)).thenReturn(accountDtoAfterIncrease);
        Mockito.when(transactionRepository.save(transactionMapper.convertToEntity(actual))).thenReturn(newTransaction);
        Mockito.when(transactionMapper.convertToTransactionDepositDto(newTransaction)).thenReturn(actual);

        Assertions.assertEquals(expected, transactionService.createDeposit(depositRequest, dummyToken));
    }

    @Test
    void createDepositWithAmountNearInvalidAmountTest(){
        TransactionDepositDto expected = new TransactionDepositDto(1.0, "This is a deposit");
        TransactionDepositRequestDto depositRequest = new TransactionDepositRequestDto(1.0, "This is a deposit", 8);
        TransactionDepositDto actual = new TransactionDepositDto(depositRequest.getAmount(), depositRequest.getDescription());

        AccountDto accountDtoBeforeIncrease = new AccountDto(1, 1, 1500.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);
        AccountDto accountDtoAfterIncrease  = new AccountDto(1, 1, 1501.0, Currency.ARS, 5000.0, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()) , false);;
        actual.setAccount(accountMapper.convertToEntity(accountDtoAfterIncrease));
        Transaction newTransaction = transactionMapper.convertToEntity(actual);

        Mockito.when(accountService.getAccountById(8)).thenReturn(accountDtoBeforeIncrease);
        Mockito.when(userService.loadUserByUsername(any())).thenReturn(new User(1));
        Mockito.when(accountService.hasUserAccountById(any(),any())).thenReturn(true);
        Mockito.when(accountService.increaseBalance(1, 1.0)).thenReturn(accountDtoAfterIncrease);
        Mockito.when(transactionRepository.save(transactionMapper.convertToEntity(actual))).thenReturn(newTransaction);
        Mockito.when(transactionMapper.convertToTransactionDepositDto(newTransaction)).thenReturn(actual);

        Assertions.assertEquals(expected, transactionService.createDeposit(depositRequest, dummyToken));
    }
}
