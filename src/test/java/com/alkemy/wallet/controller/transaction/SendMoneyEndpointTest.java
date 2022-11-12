package com.alkemy.wallet.controller.transaction;

import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.dto.TransactionTransferRequestDto;
import com.alkemy.wallet.exception.InvalidAccountCurrencyException;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.TransactionLimitExceededException;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SendMoneyEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    Timestamp timestamp = new Timestamp(new Date().getTime());
    String goodTokenUser1, goodTokenUser2, notRegisteredTokenUser;
    Transaction tr1, tr2, tr3;
    Account acc1, acc2, acc3, acc4;

    @BeforeEach
    void setUp() {

        Role rolUser = new Role(RoleName.USER, "user", timestamp, timestamp);

        User user1 = new User(1, "Marcos", "Fernandez", "email@email", "kjkjkjw", rolUser, timestamp, timestamp, false);
        User user2 = new User(2, "Maria", "Fernandez", "mar@email", "kjkjkjw", rolUser, timestamp, timestamp, false);
        User user3 = new User(3, "Dante", "Smith", "ds@email", "kjkjkjw", rolUser, timestamp, timestamp, false);

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(user2);
        when(userRepository.findByEmail(user3.getEmail())).thenReturn(null);

        when(userRepository.findById(user1.getUserId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getUserId())).thenReturn(Optional.of(user2));
        when(userRepository.findById(user3.getUserId())).thenReturn(Optional.empty());

        goodTokenUser1 = jwtUtil.generateToken(user1);
        goodTokenUser2 = jwtUtil.generateToken(user2);
        notRegisteredTokenUser = jwtUtil.generateToken(user3);

        acc1 = new Account(1, Currency.ARS, 10000d, 75000d, user1, timestamp, timestamp, false);
        acc2 = new Account(2, Currency.USD, 2000d, 35000d, user1, timestamp, timestamp, false);
        acc3 = new Account(3, Currency.ARS, 100000d, 75000d, user2, timestamp, timestamp, false);
        acc4 = new Account(4, Currency.USD, 20000d, 5000d, user2, timestamp, timestamp, false);
        when(accountRepository.findAccountByUserIdAndCurrency(acc1.getUser(), acc1.getCurrency())).thenReturn(Optional.of(acc1));
        when(accountRepository.findAccountByUserIdAndCurrency(acc2.getUser(), acc2.getCurrency())).thenReturn(Optional.of(acc2));
        when(accountRepository.findAccountByUserIdAndCurrency(acc3.getUser(), acc3.getCurrency())).thenReturn(Optional.of(acc3));
        when(accountRepository.findAccountByUserIdAndCurrency(acc4.getUser(), acc4.getCurrency())).thenReturn(Optional.of(acc4));


        when(accountRepository.findById(acc1.getAccountId())).thenReturn(Optional.of(acc1));
        when(accountRepository.findById(acc2.getAccountId())).thenReturn(Optional.of(acc2));
        when(accountRepository.findById(acc3.getAccountId())).thenReturn(Optional.of(acc3));
        when(accountRepository.findById(acc4.getAccountId())).thenReturn(Optional.of(acc4));


        when(accountRepository.findAll()).thenReturn(List.of(acc1, acc2, acc3, acc4));

        tr1 = new Transaction(1, 5000d, TransactionType.PAYMENT, "this is a ARS transfer", acc1, timestamp);
        tr2 = new Transaction(2, 5000d, TransactionType.INCOME, "this is a ARS transfer", acc2, timestamp);
        tr3 = new Transaction(3, 1500d, TransactionType.PAYMENT, "this is a USD transfer", acc2, timestamp);
        when(transactionRepository.save(any())).thenReturn(tr1);

        when(transactionRepository.findAll()).thenReturn(List.of(tr1, tr2));

        //send ars transactions
        Account acc1AfterPayment = new Account(1, Currency.ARS, 10000d, 70000d, user1, timestamp, timestamp, false);
        Account acc3AfterIncome = new Account(3, Currency.ARS, 100000d, 80000d, user2, timestamp, timestamp, false);
        when(accountRepository.save(acc1AfterPayment)).thenReturn(acc1AfterPayment);
        when(accountRepository.save(acc3AfterIncome)).thenReturn(acc3AfterIncome);


        //send usd transactions
        Account acc2AfterPayment = new Account(2, Currency.USD, 2000d, 33500d, user1, timestamp, timestamp, false);
        Account acc4AfterIncome = new Account(4, Currency.USD, 20000d, 6500d, user2, timestamp, timestamp, false);
        when(accountRepository.save(acc2AfterPayment)).thenReturn(acc2AfterPayment);
        when(accountRepository.save(acc4AfterIncome)).thenReturn(acc4AfterIncome);

    }

    @Nested
    @DisplayName("Endpoint send Ars")
    class sendArs {

        @Test
        void sendArs_and_is_okay() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(5000d, "this is a ARS transfer", 3);
            TransactionDetailDto trd1 = new TransactionDetailDto(1, 5000d, TransactionType.PAYMENT, "this is a ARS transfer", timestamp);

            mockMvc.perform(post("/transactions/sendArs")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(trd1.getAmount()))
                    .andExpect(jsonPath("$.description").value(trd1.getDescription()))
                    .andExpect(jsonPath("$.id").value(trd1.getTransactionID()))
                    .andExpect(jsonPath("$.type").value(trd1.getType().toString()))
            ;
        }


        @Test
        void sendArs_exception_between_different_currencyAccounts() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(5000d, "this is a ARS transfer", 4);

            mockMvc.perform(post("/transactions/sendArs")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAccountCurrencyException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "The transfer currency must be equal to the currency of the receiver account."))
            ;
        }


        @Test
        void sendArs_to_an_account_that_dont_exist() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(5000d, "this is a ARS transfer", 5);
            when(accountRepository.findById(5)).thenReturn(Optional.empty());

            mockMvc.perform(post("/transactions/sendArs")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                            "The account with id: " + transactionTransferRequestDto.getAccountId() + " was not found"))
            ;
        }


        @Test
        void sendArs_more_than_transactionLimit() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(15000d, "this is a ARS transfer", 3);

            mockMvc.perform(post("/transactions/sendArs")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TransactionLimitExceededException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                            "The balance reduction of " + transactionTransferRequestDto.getAmount()
                                    + " exceeded the transaction limit of " + acc1.getTransactionLimit()))
            ;
        }


        @Test
        void sendArs_without_founds() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(90000d, "this is a ARS transfer", 1);

            mockMvc.perform(post("/transactions/sendArs")
                            .header("Authorization", "Bearer " + goodTokenUser2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAmountException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "The amount to reduce is bigger than the current balance."))
            ;
        }

    }


    @Nested
    @DisplayName("Endpoint send Usd")
    class sendUsd {

        @Test
        void sendUsd_and_is_okay() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(1500d, "this is a USD transfer", 4);
            TransactionDetailDto trd1 = new TransactionDetailDto(3, 1500d, TransactionType.PAYMENT, "this is a USD transfer", timestamp);

            when(transactionRepository.save(any())).thenReturn(tr3);

            mockMvc.perform(post("/transactions/sendUsd")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(trd1.getAmount()))
                    .andExpect(jsonPath("$.description").value(trd1.getDescription()))
                    .andExpect(jsonPath("$.id").value(trd1.getTransactionID()))
                    .andExpect(jsonPath("$.type").value(trd1.getType().toString()))
            ;
        }


        @Test
        void sendUsd_exception_between_different_currencyAccounts() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(1500d, "this is a USD transfer", 3);

            mockMvc.perform(post("/transactions/sendUsd")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAccountCurrencyException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "The transfer currency must be equal to the currency of the receiver account."))
            ;
        }


        @Test
        void sendUsd_to_an_account_that_dont_exist() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(1500d, "this is a USD transfer", 5);
            when(accountRepository.findById(5)).thenReturn(Optional.empty());

            mockMvc.perform(post("/transactions/sendUsd")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                            "The account with id: " + transactionTransferRequestDto.getAccountId() + " was not found"))
            ;
        }


        @Test
        void sendUsd_more_than_transactionLimit() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(3000d, "this is a USD transfer", 4);

            mockMvc.perform(post("/transactions/sendUsd")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TransactionLimitExceededException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                            "The balance reduction of " + transactionTransferRequestDto.getAmount()
                                    + " exceeded the transaction limit of " + acc2.getTransactionLimit()))
            ;
        }

        @Test
        void sendUsd_without_founds() throws Exception {
            TransactionTransferRequestDto transactionTransferRequestDto = new TransactionTransferRequestDto(10000d, "this is a USD transfer", 2);

            mockMvc.perform(post("/transactions/sendUsd")
                            .header("Authorization", "Bearer " + goodTokenUser2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transactionTransferRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAmountException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "The amount to reduce is bigger than the current balance."))
            ;
        }
    }
}