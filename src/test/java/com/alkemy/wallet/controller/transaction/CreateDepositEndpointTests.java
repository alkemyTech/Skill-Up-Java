package com.alkemy.wallet.controller.transaction;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDepositRequestDto;
import com.alkemy.wallet.exception.ForbiddenAccessException;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.TransactionLimitExceededException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateDepositEndpointTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionMapper transactionMapper;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    Timestamp timestamp = new Timestamp(new Date().getTime());

    String token1, token2;

    User user1, user2;

    Double invalidDeposit1;

    AccountDto accountDtoBeforeIncrease1, accountDtoBeforeIncrease2;

    TransactionDepositDto expectedTransactionDepositDto;

    private TransactionDepositRequestDto depositRequestDto;


    @BeforeEach
    void setUp() {
        // Set up of Users
        Role userRole = new Role(RoleName.USER, "user", timestamp, timestamp);

        user1 = new User(1, "Pepe", "Lopez", "pepe@example", "123", userRole, timestamp, timestamp, false);
        user2 = new User(2, "Martin", "Gomez", "gomez@example", "123", userRole, timestamp, timestamp, false);

        // Token generation and validation
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.findById(user1.getUserId())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(user2);
        when(userRepository.findById(user2.getUserId())).thenReturn(Optional.of(user2));
        
        token1 = jwtUtil.generateToken(user1);
        token2 = jwtUtil.generateToken(user2);

        // Set up of Accounts
        accountDtoBeforeIncrease1 = new AccountDto(1, user1.getUserId(), 2000.0, Currency.ARS, 5000.0, timestamp, timestamp, false);
        accountDtoBeforeIncrease2 = new AccountDto(2, user2.getUserId(), 1330.0, Currency.ARS, 5000.0, timestamp, timestamp, false);

        when(accountService.getAccountById(accountDtoBeforeIncrease1.id())).thenReturn(accountDtoBeforeIncrease1);
        when(accountService.getAccountById(accountDtoBeforeIncrease2.id())).thenReturn(accountDtoBeforeIncrease2);

        // User - Account correspondence
        // hasUserAccountById always true by default
        when(accountService.hasUserAccountById(anyInt(), anyInt())).thenReturn(true);

    }

    @Test
    void userDoesntOwnAccountTest() throws Exception{
        when(accountService.hasUserAccountById(user1.getUserId(), accountDtoBeforeIncrease2.id())).thenReturn(false);

        TransactionDepositRequestDto depositRequestDto = new TransactionDepositRequestDto(200.0, "This is a valid deposit", accountDtoBeforeIncrease2.id());

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenAccessException.class, result.getResolvedException()));
        ;
    }

    @Test
    void invalidAmountTest() throws Exception {

        TransactionDepositRequestDto depositRequestDto = new TransactionDepositRequestDto(0.0, "This is a deposit with invalid amount", accountDtoBeforeIncrease1.id());

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(InvalidAmountException.class, result.getResolvedException()));
        ;
    }

    @Test
    void amountExceedsTransactionLimitTest() throws Exception{
        invalidDeposit1 = accountDtoBeforeIncrease1.transactionLimit() + 1;

        TransactionDepositRequestDto depositRequestDto = new TransactionDepositRequestDto(invalidDeposit1, "This is a deposit with invalid transaction limit", accountDtoBeforeIncrease1.id());

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(TransactionLimitExceededException.class, result.getResolvedException()));
        ;
    }

    @Test
    void accountDoesntExist() throws Exception{
        when(accountService.getAccountById(100)).thenThrow(new ResourceNotFoundException("The account with id: " + 100 + " was not found"));

        TransactionDepositRequestDto depositRequestDto = new TransactionDepositRequestDto(invalidDeposit1, "This is a deposit with invalid transaction limit", 100);

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
        ;
    }

    void setUpForValidCreateDeposit(Double amount, AccountDto accountDtoBeforeIncrease){
        AccountDto accountDtoAfterIncrease = new AccountDto(
                accountDtoBeforeIncrease.id(),
                accountDtoBeforeIncrease.userId(),
                accountDtoBeforeIncrease.balance() + amount,
                accountDtoBeforeIncrease.currency(),
                accountDtoBeforeIncrease.transactionLimit(), timestamp, timestamp, false);

        when(accountService.increaseBalance(accountDtoBeforeIncrease.id(), amount)).thenReturn(accountDtoAfterIncrease);

        depositRequestDto = new TransactionDepositRequestDto(amount, "This is a valid deposit", accountDtoBeforeIncrease.id());
        expectedTransactionDepositDto = new TransactionDepositDto(depositRequestDto.getAmount(), depositRequestDto.getDescription());

        Transaction expectedTransactionDepositEntity = transactionMapper.convertToEntity(expectedTransactionDepositDto);

        when(transactionRepository.save(expectedTransactionDepositEntity)).thenReturn(expectedTransactionDepositEntity);
        when(transactionMapper.convertToTransactionDepositDto(expectedTransactionDepositEntity)).thenReturn(expectedTransactionDepositDto);
    }

    // Happy path
    @Test
    void createDepositTest() throws Exception{

        setUpForValidCreateDeposit(3000.0, accountDtoBeforeIncrease2);

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(expectedTransactionDepositDto.getAmount()))
                .andExpect(jsonPath("$.description").value(expectedTransactionDepositDto.getDescription()));
        ;
    }

    // Border values

    @Test
    void createDepositWithAmountNearTransactionLimitTest() throws Exception{

        setUpForValidCreateDeposit(accountDtoBeforeIncrease1.transactionLimit() - 1, accountDtoBeforeIncrease1);

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(expectedTransactionDepositDto.getAmount()))
                .andExpect(jsonPath("$.description").value(expectedTransactionDepositDto.getDescription()));
        ;
    }

    @Test
    void createDepositWithAmountNearInvalidAmountTest() throws Exception{

        setUpForValidCreateDeposit(1.0, accountDtoBeforeIncrease2);

        mockMvc.perform(post("/transactions/deposit")
                        .header("Authorization", "Bearer " + token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(expectedTransactionDepositDto.getAmount()))
                .andExpect(jsonPath("$.description").value(expectedTransactionDepositDto.getDescription()));
        ;
    }
}
