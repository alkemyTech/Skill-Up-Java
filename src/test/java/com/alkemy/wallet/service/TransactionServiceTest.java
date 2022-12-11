package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private AccountDto accountUsdDto, accountArsDto;
    private TransactionDto transactionDeposit, transactionBetweenUsdAccounts, transactionBetweenArsAccounts;

    private Transaction transaction = new Transaction();

    @Autowired
    private Mapper mapper;

    @Mock
    private IAccountRepository accountRespository;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzAzNzgxNTcsInN1YiI6InZpY3RvcmlvMjMuc2FybmFnbGlhQGdtYWlsLmNvbSIsImlzcyI6Ik1haW4iLCJleHAiOjE2NzA5ODI5NTd9.BoAQrh05wgiukTIKAvpUQApCNykgiCiaNSFCzOPtoJo";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        accountArsDto = new AccountDto();
        accountArsDto.setCurrency(Currency.usd);
        accountArsDto.setUserId(1L);

        accountUsdDto = new AccountDto();
        accountUsdDto.setCurrency(Currency.ars);
        accountUsdDto.setUserId(2L);
        accountUsdDto.setId(2L);

        transactionDeposit = new TransactionDto();
        transactionDeposit.setDescription("Descripcion de prueba");
        transactionDeposit.setAmount(1000.0);
        transactionDeposit.setTransactionDate(new Date());
        transactionDeposit.setAccount(accountUsdDto);

        transactionBetweenUsdAccounts = new TransactionDto();
        transactionBetweenUsdAccounts.setDescription("Descripcion de prueba USD");
        transactionBetweenUsdAccounts.setAmount(50.);
        transactionBetweenUsdAccounts.setTransactionDate(new Date());
        transactionBetweenUsdAccounts.setAccount(accountUsdDto);

        transactionBetweenArsAccounts = new TransactionDto();
        transactionBetweenArsAccounts.setDescription("Descripcion prueba ARS");
        transactionBetweenArsAccounts.setAmount(200.);
        transactionBetweenArsAccounts.setTransactionDate(new Date());
        transactionBetweenArsAccounts.setAccount(accountArsDto);


    }

    @Test
    void createDeposit() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        assertNotNull(transactionService.createDeposit(transactionDeposit));
    }


//    @Test
//    void createTransactionsUsd() {
//        when(transactionService.makeTransaction(token, transactionBetweenUsdAccounts)).thenReturn();
//    }
//
//    @Test
//    void createTransactionsArs() {
//        when(transactionService.makeTransaction(token, transactionBetweenArsAccounts)).thenReturn();
//    }
}