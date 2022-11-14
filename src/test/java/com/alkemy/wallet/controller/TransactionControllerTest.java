/*
package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionControllerTest {
    private final ITransactionRepository repository = Mockito.mock(ITransactionRepository.class);
    private final IAuthService authService = Mockito.mock(IAuthService.class);
    private final IUserService userService = Mockito.mock(IUserService.class);
    private final IAccountService accountService = Mockito.mock(IAccountService.class);
    private final ITransactionService transactionService = new TransactionServiceImpl(repository, userService, authService, accountService);
    private final ModelMapper mapper = new ModelMapper();
    private final TransactionController controller = new TransactionController(mapper,transactionService);

    String token;
    User user1;
    User user2;
    Account account1;
    Account account2;

    @BeforeEach
    void setUp() {
        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnJveW9sb2t1cmFAZ21haWwuY29tIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNjY4MDMxNjk4LCJpYXQiOjE2Njc5OTU2OTh9.tJ_rksi7lC4vldQPbiK83bntFgSIcgxKEz8u3OnBv-U";

        user1 = new User(1L, "user", "user", "user@gmail.com",
                "$10$HqJv0Vzw0u6WzPss7JTTzuYazYo7qaQEjUBPkKSauSAlKeMyXt0Dm", LocalDateTime.now(),
                null, false, null, null, null, null);

        user2 = new User(2L, "admin", "admin", "admin@gmail.com",
                "$10$HqJv0Vzw0u6WzPss7JTTzuYazYo7qaQEjUBPkKSauSAlKeMyXt0Dm", LocalDateTime.now(),
                null, false, null, null, null, null);

        account1 = new Account(1L, null, 1500D, 3000D, LocalDateTime.now(),
                null, false, user1, null, null);

        account2 = new Account(2L, null, 1500D, 3000D, LocalDateTime.now(),
                null, false, user2, null, null);
    }

    @Test
    void moneySendInPesos() {
        account1.setCurrency(AccountCurrencyEnum.ARS);
        account2.setCurrency(AccountCurrencyEnum.ARS);

        Mockito.when(authService.getUserFromToken(token)).thenReturn(user2);

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user1));

        Mockito.when(userService.findById(2L)).thenReturn(Optional.of(user2));

        Mockito.when(accountService.findTopByUserId(1L)).thenReturn(Optional.of(account1));

        Mockito.when(accountService.findTopByUserId(2L)).thenReturn(Optional.of(account2));

        ResponseEntity<Map<String, String>> response = controller.moneySendInPesos(1L, 1000D, "INCOME", token);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(new ResponseEntity<>(Map.of("Message", "operation performed successfully"), HttpStatus.ACCEPTED), response);

    }

    @Test
    void moneySendInUsd() {
        account1.setCurrency(AccountCurrencyEnum.USD);
        account2.setCurrency(AccountCurrencyEnum.USD);

        Mockito.when(authService.getUserFromToken(token)).thenReturn(user2);

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user1));

        Mockito.when(userService.findById(2L)).thenReturn(Optional.of(user2));

        Mockito.when(accountService.findTopByUserId(1L)).thenReturn(Optional.of(account1));

        Mockito.when(accountService.findTopByUserId(2L)).thenReturn(Optional.of(account2));

        ResponseEntity<Map<String, String>> response = controller.moneySendInUsd(1L, 1000D, "INCOME", token);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(new ResponseEntity<>(Map.of("Message", "operation performed successfully"), HttpStatus.ACCEPTED), response);
    }
}*/
