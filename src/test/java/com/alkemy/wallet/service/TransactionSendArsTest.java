package com.alkemy.wallet.service;

import com.alkemy.wallet.controller.AuthController;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import({ObjectMapper.class, AuthController.class})
@TestPropertySource(locations = "classpath:applicationtest.properties")
class TransactionSendArsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITransactionRepository transactionRepository;

    @MockBean
    private IAccountRepository accountRespository;

    @MockBean
    private IUserRepository userRepository;
    @InjectMocks
    private TransactionService transactionService;

    @MockBean
    private IUserService userService;
    @MockBean
    private IAccountService accountService;

    private TransactionDto transactionBetweenUsdAccounts;

    private Transaction transaction;
    private Account senderAccountTest, receivingAccountTest;
    private User userTest, userTest2;
    private TransactionDto transactionBetweenArsAccounts;

    @Autowired
    private Mapper mapper;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;
    private List<Account> accountsTest;


    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzA3MTE2NDAsInN1YiI6InZpY3RvcmlvLnNhcm5hZ2xpYUBnbWFpbC5jb20iLCJpc3MiOiJNYWluIiwiZXhwIjoxNjcxMzE2NDQwfQ.q3fFXiarnJo1FqBg4_WbzCDvui3BSdITmIYrzhCBw44";


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
                .currency(Currency.ars)
                .transactionLimit(3000.).build();

        receivingAccountTest = Account.builder()
                .id(2L)
                .balance(3000.)
                .user(userTest2)
                .currency(Currency.ars)
                .transactionLimit(3000.).build();

        accountsTest = new ArrayList<>();
        accountsTest.add(senderAccountTest);

        transactionBetweenArsAccounts = new TransactionDto();
        transactionBetweenArsAccounts.setDescription("Descripcion prueba ARS");
        transactionBetweenArsAccounts.setAmount(200.);
        transactionBetweenArsAccounts.setTransactionDate(new Date());
        transactionBetweenArsAccounts.setAccount(mapper.getMapper().map(receivingAccountTest, AccountDto.class));

        transaction = mapper.getMapper().map(transactionBetweenArsAccounts, Transaction.class);

        when(userRepository.findById(userTest.getId())).thenReturn(Optional.ofNullable(userTest));
        when(accountRespository.findById(anyLong())).thenReturn(Optional.ofNullable(receivingAccountTest));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(userRepository.findByEmail(jwtUtil.getValue(token))).thenReturn(userTest);
        when(accountRespository.findAllByUser_Id(anyLong())).thenReturn(accountsTest);
        when(accountService.getAccountByCurrency(userTest.getId(), Currency.ars)).thenReturn(senderAccountTest);
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "test")
    void createTransactionsArs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/sendArs")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionBetweenArsAccounts)))
                .andExpect(status().isOk());
    }

    @Test
    void createTransactionsArsWithoutAuthorization() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/sendArs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionBetweenArsAccounts)))
                .andExpect(status().isBadRequest());
    }
}
