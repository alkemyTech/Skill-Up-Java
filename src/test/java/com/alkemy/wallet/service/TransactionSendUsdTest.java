package com.alkemy.wallet.service;

import com.alkemy.wallet.controller.AuthController;
import com.alkemy.wallet.controller.TransactionsController;
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration
@Import({ObjectMapper.class, TransactionsController.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class TransactionSendUsdTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ITransactionRepository transactionRepository;
    @MockBean
    private IAccountRepository accountRespository;
    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private IUserService userService;
    @MockBean
    private IAccountService accountService;
    @MockBean
    private JwtUtil jwtUtil;
    @InjectMocks
    private TransactionService transactionService;
    private User userTest, userTest2;
    private TransactionDto transactionBetweenUsdAccounts;
    private Transaction transaction = new Transaction();

    private TransactionDto transactionDto;
    private Account senderAccountTest, receivingAccountTest;

    @Autowired
    Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Account> accountsTest;
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzA4Njc3NTMsInN1YiI6InZpY3RvcmlvLnNhcm5hZ2xpYUBnbWFpbC5jb20iLCJpc3MiOiJNYWluIiwiZXhwIjoxNjcxNDcyNTUzfQ.-LQO6GnpJu7IPij-U6np15gVzT5sRQWQ1y_IeelcrCU";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userTest = User.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .password("test").build();

        userTest2 = User.builder()
                .id(2L)
                .firstName("test2")
                .lastName("test2")
                .email("test2@test.com")
                .password("test2").build();

        senderAccountTest = Account.builder()
                .id(1L)
                .balance(3000.)
                .user(userTest)
                .currency(Currency.usd)
                .transactionLimit(3000.).build();

        receivingAccountTest = Account.builder()
                .id(2L)
                .balance(3000.)
                .user(userTest2)
                .currency(Currency.usd)
                .transactionLimit(3000.).build();

        accountsTest = new ArrayList<>();
        accountsTest.add(senderAccountTest);

        transactionBetweenUsdAccounts = new TransactionDto();
        transactionBetweenUsdAccounts.setDescription("Descripcion de prueba USD");
        transactionBetweenUsdAccounts.setAmount(50.);
        transactionBetweenUsdAccounts.setTransactionDate(new Date());
        transactionBetweenUsdAccounts.setAccount(mapper.getMapper().map(receivingAccountTest, AccountDto.class));

        transaction = mapper.getMapper().map(transactionBetweenUsdAccounts, Transaction.class);

        when(userRepository.findById(userTest.getId())).thenReturn(Optional.ofNullable(userTest));
        when(accountRespository.findById(anyLong())).thenReturn(Optional.ofNullable(receivingAccountTest));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(userRepository.findByEmail(jwtUtil.getValue(token))).thenReturn(userTest);
        when(accountRespository.findAllByUser_Id(anyLong())).thenReturn(accountsTest);
        when(accountService.getAccountByCurrency(userTest.getId(), Currency.usd)).thenReturn(senderAccountTest);


    }

    @Test
    @WithMockUser(username = "test@test.com", password = "test")
    void createTransactionsUsd() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/sendUsd")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionBetweenUsdAccounts)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void createTransactionsUsdWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/sendUsd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionBetweenUsdAccounts)))
                .andExpect(status().isBadRequest());
    }
}
