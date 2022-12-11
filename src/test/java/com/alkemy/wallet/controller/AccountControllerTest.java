package com.alkemy.wallet.controller;

import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IFixedTermRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.util.DataLoaderUser;
import com.alkemy.wallet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
class AccountControllerTest {

    @MockBean
    IAccountRepository accountRepository;
    @MockBean
    DataLoaderUser dataLoaderUser;
    @MockBean
    IUserRepository userRepository;
    @MockBean
    IFixedTermRepository fixedTermRepository;
    User user;
    Role role;
    Account accountArs;
    Account accountUsd;
    FixedTermDeposit fixedTermArs;
    FixedTermDeposit fixedTermUsdOne;
    FixedTermDeposit fixedTermUsdTwo;
    String token;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        role = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_USER)
                .description("Users can access to the following operations:")
                .creationDate(new Date())
                .build();

        user = User.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .password("test")
                .creationDate(new Date())
                .role(role)
                .build();

        accountArs = Account.builder()
                .id(1L)
                .balance(100000D)
                .transactionLimit(300000D)
                .creationDate(new Date())
                .user(user)
                .currency(Currency.ars)
                .build();

        accountUsd = Account.builder()
                .id(2L)
                .balance(1000D)
                .transactionLimit(1000D)
                .creationDate(new Date())
                .user(user)
                .currency(Currency.usd)
                .build();

        fixedTermArs = FixedTermDeposit.builder()
                .id(1L)
                .account(accountArs)
                .amount(20000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(35))
                .interest(0.005)
                .build();

        fixedTermUsdOne = FixedTermDeposit.builder()
                .id(2L)
                .account(accountUsd)
                .amount(500D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(35))
                .interest(0.005)
                .build();

        fixedTermUsdTwo = FixedTermDeposit.builder()
                .id(3L)
                .account(accountUsd)
                .amount(500D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(35))
                .interest(0.005)
                .build();

        token = jwtUtil.create(user.getEmail());
    }


    @Test
    void when_getBalance_successfully() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        when(accountRepository.findAllByUser_Email(anyString())).thenReturn(List.of(accountArs, accountUsd));

        when(fixedTermRepository.findAllByAccount_Id(accountArs.getId())).thenReturn(List.of(fixedTermArs));

        when(fixedTermRepository.findAllByAccount_Id(accountUsd.getId())).thenReturn(List.of(fixedTermUsdOne, fixedTermUsdTwo));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/balance")
                        .with(csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].balance", is(100000D)))
                .andExpect(jsonPath("$[0].currency", is("ars")))
                .andExpect(jsonPath("$[0].fixedTerm[0].id", is(1)))
                .andExpect(jsonPath("$[0].fixedTerm[0].amount", is(20000D)))
                .andExpect(jsonPath("$[0].fixedTerm[0].creationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$[0].fixedTerm[0].closingDate", is(LocalDate.now().plusDays(35).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].balance", is(1000D)))
                .andExpect(jsonPath("$[1].currency", is("usd")))
                .andExpect(jsonPath("$[1].fixedTerm[0].id", is(2)))
                .andExpect(jsonPath("$[1].fixedTerm[0].amount", is(500D)))
                .andExpect(jsonPath("$[1].fixedTerm[0].creationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$[1].fixedTerm[0].closingDate", is(LocalDate.now().plusDays(35).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$[1].fixedTerm[1].id", is(3)))
                .andExpect(jsonPath("$[1].fixedTerm[1].amount", is(500D)))
                .andExpect(jsonPath("$[1].fixedTerm[1].creationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$[1].fixedTerm[1].closingDate", is(LocalDate.now().plusDays(35).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))));


    }
}