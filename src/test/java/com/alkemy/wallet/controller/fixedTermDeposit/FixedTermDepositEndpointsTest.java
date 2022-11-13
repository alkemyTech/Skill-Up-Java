package com.alkemy.wallet.controller.fixedTermDeposit;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDto;
import com.alkemy.wallet.exception.FixedTermDepositException;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FixedTermDepositEndpointsTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private FixedTermDepositRepository fixedTermDepositRepository;
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
    String goodTokenUser1, goodTokenUser2;
    Account acc1, acc2;

    final long daysToMilisecs = 86400000;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    @BeforeEach
    void setUp() {

        Role rolUser = new Role(RoleName.USER, "user", timestamp, timestamp);

        User user1 = new User(1, "Marcos", "Fernandez", "email@email", "kjkjkjw", rolUser, timestamp, timestamp, false);
        User user2 = new User(2, "Maria", "Fernandez", "mar@email", "kjkjkjw", rolUser, timestamp, timestamp, false);

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(user2);

        when(userRepository.findById(user1.getUserId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getUserId())).thenReturn(Optional.of(user2));

        goodTokenUser1 = jwtUtil.generateToken(user1);
        goodTokenUser2 = jwtUtil.generateToken(user2);

        acc1 = new Account(1, Currency.ARS, 10000d, 75000d, user1, timestamp, timestamp, false);
        acc2 = new Account(2, Currency.ARS, 10000d, 100d, user2, timestamp, timestamp, false);

        when(accountRepository.findAccountByUserIdAndCurrency(acc1.getUser(), acc1.getCurrency())).thenReturn(Optional.of(acc1));
        when(accountRepository.findAccountByUserIdAndCurrency(acc2.getUser(), acc2.getCurrency())).thenReturn(Optional.of(acc2));
        when(accountRepository.findById(acc1.getAccountId())).thenReturn(Optional.of(acc1));
        when(accountRepository.findById(acc2.getAccountId())).thenReturn(Optional.of(acc2));

        Account acc2AfterDeposit = new Account(1, Currency.ARS, 10000d, 72000d, user1, timestamp, timestamp, false);
        when(accountRepository.save(acc2AfterDeposit)).thenReturn(acc2AfterDeposit);

    }


    @Nested
    @DisplayName("Endpoint to create a fixed term deposit")
    class createFixedTermDeposit {

        //TODO create okay fixed term deposit
        @Test
        void create_fixedTermDeposit_and_is_okay() throws Exception {
            Timestamp timestamp1 = new Timestamp(timestamp.getTime() + daysToMilisecs * 40);

            String simpleClosingDate = new SimpleDateFormat("yyyy-MM-dd").format(timestamp1);
            Timestamp closingDateReq = new Timestamp(simpleDateFormat.parse(simpleClosingDate).getTime());

            FixedTermDepositDto requestFixedTerm = new FixedTermDepositDto(3000d, closingDateReq, Currency.ARS);

            mockMvc.perform(post("/fixedDeposit/create")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestFixedTerm)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(requestFixedTerm.getAmount()))
                    .andExpect(jsonPath("$.closingDate").value(simpleClosingDate))
                    .andExpect(jsonPath("$.currency").value(requestFixedTerm.getCurrency().toString()))
            ;
        }


        @Test
        void create_fixedTermDeposit_should_get_exception_if_duration_Is_lesser_than_30Days() throws Exception {
            Timestamp timestamp1 = new Timestamp(timestamp.getTime() + daysToMilisecs * 10);
            FixedTermDepositDto fixedTermDepositRequestDto = new FixedTermDepositDto(3000d, timestamp1, Currency.ARS);

            mockMvc.perform(post("/fixedDeposit/create")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fixedTermDepositRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof FixedTermDepositException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "the fixed plan entered, it has less than 30 days"))
            ;
        }

        @Test
        void create_fixedTermDeposit_should_get_exception_if_account_dont_have_amount() throws Exception {
            Timestamp timestamp1 = new Timestamp(timestamp.getTime() + daysToMilisecs * 40);

            String simpleClosingDate = new SimpleDateFormat("yyyy-MM-dd").format(timestamp1);
            Timestamp closingDateReq = new Timestamp(simpleDateFormat.parse(simpleClosingDate).getTime());

            FixedTermDepositDto requestFixedTerm = new FixedTermDepositDto(3000d, closingDateReq, Currency.ARS);

            mockMvc.perform(post("/fixedDeposit/create")
                            .header("Authorization", "Bearer " + goodTokenUser2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestFixedTerm)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAmountException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "The amount to reduce is bigger than the current balance."))
            ;
        }


    }


    @Nested
    @DisplayName("Endpoint to simulate a fixed term deposit")
    class simulateFixedTermDeposit {
        //TODO simulate okay fixed term deposit
        @Test
        void simulate_fixedTermDeposit_and_is_okay() throws Exception {
            Timestamp timestamp1 = new Timestamp(timestamp.getTime() + daysToMilisecs * 40);

            String simpleClosingDate = new SimpleDateFormat("yyyy-MM-dd").format(timestamp1);
            Timestamp closingDateReq = new Timestamp(simpleDateFormat.parse(simpleClosingDate).getTime());

            FixedTermDepositDto requestFixedTerm = new FixedTermDepositDto(3000d, closingDateReq, Currency.ARS);

            long days = (requestFixedTerm.getClosingDate().getTime() - timestamp.getTime())/86400000;
            FixedTermDepositSimulateDto expectedFixedTerm = new FixedTermDepositSimulateDto(
                    requestFixedTerm.getAmount(),
                    requestFixedTerm.getClosingDate(),
                    timestamp,
                    requestFixedTerm.getCurrency(),
                    0.5*days,
                    days*0.005*requestFixedTerm.getAmount()+requestFixedTerm.getAmount()
            );

            mockMvc.perform(get("/fixedDeposit/simulate")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestFixedTerm)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(expectedFixedTerm.getAmount()))
                    .andExpect(jsonPath("$.closingDate").value(simpleClosingDate))
                    .andExpect(jsonPath("$.currency").value(expectedFixedTerm.getCurrency().toString()))
                    .andExpect(jsonPath("$.interest").value(expectedFixedTerm.getInterest()))
                    .andExpect(jsonPath("$.totalAmount").value(expectedFixedTerm.getTotalAmount()))
            ;
        }

        //TODO should fail if the duration is lesser than 30 days
        @Test
        void simulate_fixedTermDeposit_should_get_exception_if_duration_Is_lesser_than_30Days() throws Exception {
            Timestamp timestamp1 = new Timestamp(timestamp.getTime() + daysToMilisecs * 10);
            FixedTermDepositDto fixedTermDepositRequestDto = new FixedTermDepositDto(3000d, timestamp1, Currency.ARS);

            mockMvc.perform(get("/fixedDeposit/simulate")
                            .header("Authorization", "Bearer " + goodTokenUser1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fixedTermDepositRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof FixedTermDepositException))
                    .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), "the fixed plan entered, it has less than 30 days"))
            ;
        }
    }

}