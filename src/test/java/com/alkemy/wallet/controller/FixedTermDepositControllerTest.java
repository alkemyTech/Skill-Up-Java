package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDto;
import com.alkemy.wallet.exception.FixedTermException;
import com.alkemy.wallet.exception.NotEnoughCashException;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IFixedTermRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.util.DataLoaderUser;
import com.alkemy.wallet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
class FixedTermDepositControllerTest {

    static final Integer MIN_DAYS = 30;
    @MockBean
    DataLoaderUser dataLoaderUser;
    @MockBean
    IUserRepository userRepository;
    @MockBean
    IFixedTermRepository fixedTermRepository;
    User user;
    Role role;
    Account account;
    String token;
    @MockBean
    IAccountRepository accountRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userService;

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

        account = Account.builder()
                .id(1L)
                .balance(10000D)
                .transactionLimit(300000D)
                .creationDate(new Date())
                .user(user)
                .currency(Currency.ars)
                .build();

        token = jwtUtil.create(user.getEmail());
    }

    @Test
    void when_createFixedDeposit_successfully() throws Exception {

        FixedTermDto fixedTermDto = FixedTermDto.builder()
                .accountId(1L)
                .amount(2000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(35))
                .currency(Currency.ars)
                .build();

        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder()
                .id(1L)
                .account(account)
                .amount(2000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(35))
                .interest(0.005)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        when(accountRepository.findByCurrencyAndUser_Email(any(Currency.class), anyString())).thenReturn(account);

        when(fixedTermRepository.save(any(FixedTermDeposit.class))).thenReturn(fixedTermDeposit);

        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(account));

        mockMvc.perform(MockMvcRequestBuilders.post("/fixedDeposit")
                        .with(csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fixedTermDto)))

                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.amount", is(2000D)))
                .andExpect(jsonPath("$.accountId", is(1)))
                .andExpect(jsonPath("$.interest", is(0.005)))
                .andExpect(jsonPath("$.creationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$.closingDate", is(LocalDate.now().plusDays(35).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$.currency", is("ars")));

    }

    @Test
    void when_createFixedDeposit_fails_withPeriodLessThan30Days() throws Exception {

        FixedTermDto fixedTermDto = FixedTermDto.builder()
                .accountId(1L)
                .amount(2000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(25))
                .currency(Currency.ars)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        when(accountRepository.findByCurrencyAndUser_Email(any(Currency.class), anyString())).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/fixedDeposit")
                        .with(csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fixedTermDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FixedTermException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                        "Closing Date must be greater or equal to " + MIN_DAYS + " days"));

    }

    @Test
    void when_createFixedDeposit_withNotEnoughCash() throws Exception {

        FixedTermDto fixedTermDto = FixedTermDto.builder()
                .accountId(1L)
                .amount(200000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(60))
                .currency(Currency.ars)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        when(accountRepository.findByCurrencyAndUser_Email(any(Currency.class), anyString())).thenReturn(account);

        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(account));

        mockMvc.perform(MockMvcRequestBuilders.post("/fixedDeposit")
                        .with(csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fixedTermDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotEnoughCashException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                        "Not enough cash"));
    }

    @Test
    void when_simulateFixedDeposit_successfully() throws Exception {
        FixedTermDto fixedTermDto = FixedTermDto.builder()
                .accountId(1L)
                .amount(2000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(30))
                .currency(Currency.ars)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/fixedTermDeposit/simulate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fixedTermDto)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount", is(2000D)))
                .andExpect(jsonPath("$.interest", is(300D)))
                .andExpect(jsonPath("$.totalAmount", is(2300D)))
                .andExpect(jsonPath("$.creationDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))))
                .andExpect(jsonPath("$.closingDate", is(LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))));
    }

    @Test
    void when_simulateFixedDeposit_withPeriodLessThan30Days() throws Exception {
        FixedTermDto fixedTermDto = FixedTermDto.builder()
                .accountId(1L)
                .amount(2000D)
                .creationDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(25))
                .currency(Currency.ars)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/fixedTermDeposit/simulate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fixedTermDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FixedTermException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),
                        "Closing Date must be greater or equal to " + MIN_DAYS + " days"));
    }

}