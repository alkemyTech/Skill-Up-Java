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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
class TransactionServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ITransactionRepository transactionRepository;

    @MockBean
    private IAccountRepository accountRespository;

    @InjectMocks
    private TransactionService transactionService;

    @MockBean
    private IUserRepository userRepository;

    private AccountDto accountUsdDto, accountArsDto;
    private Account accountTest;
    private TransactionDto transactionDeposit,transactionDepositWithNoAmount;

    private Transaction transaction = new Transaction();

    private User userTest;
    @Autowired
    private Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);


        userTest = User.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .password("test").build();

        accountTest = Account.builder()
                .id(1L)
                .balance(0.)
                .user(userTest)
                .currency(Currency.ars)
                .transactionLimit(3000.).build();


        transactionDeposit = new TransactionDto();
        transactionDeposit.setDescription("Descripcion de prueba");
        transactionDeposit.setAmount(1000.0);
        transactionDeposit.setTransactionDate(new Date());
        transactionDeposit.setAccount(mapper.getMapper().map(accountTest, AccountDto.class));

        transactionDepositWithNoAmount= new TransactionDto();
        transactionDepositWithNoAmount.setDescription("Descripcion de prueba error");
        transactionDepositWithNoAmount.setAmount(0.);
        transactionDepositWithNoAmount.setTransactionDate(new Date());
        transactionDepositWithNoAmount.setAccount(mapper.getMapper().map(accountTest, AccountDto.class));

    }


    @Test
    void createDeposit() throws Exception {
        when(userRepository.findById(userTest.getId())).thenReturn(Optional.ofNullable(userTest));
        when(accountRespository.findById(anyLong())).thenReturn(Optional.ofNullable(accountTest));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/deposit")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDeposit)))
                .andExpect(status().isAccepted());
    }

    @Test
    void createDespositWithoutAmount() throws Exception {
        when(userRepository.findById(userTest.getId())).thenReturn(Optional.ofNullable(userTest));
        when(accountRespository.findById(anyLong())).thenReturn(Optional.ofNullable(accountTest));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/deposit")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDepositWithNoAmount)))
                .andExpect(status().isForbidden());
    }
}