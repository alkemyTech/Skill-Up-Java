/*
package com.alkemy.wallet.controller;

import com.alkemy.wallet.auth.service.UserDetailsCustomService;
import com.alkemy.wallet.auth.utility.JwtUtils;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.AccountMapper;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import com.alkemy.wallet.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountControllerTest {
    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final AccountMapper accountMapper = new AccountMapper();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private final IUserRepository userRepository = Mockito.mock(IUserRepository.class);
    private final UserDetailsCustomService userDetailsCustomService = new UserDetailsCustomService(userRepository);
    private final JwtUtils jwtUtils = new JwtUtils();
    private final UserMapper mapper = new UserMapper();
    private final IRoleRepository roleRepository = Mockito.mock(IRoleRepository.class);
    private final IAccountService accountService = Mockito.mock(IAccountService.class);
    private final IAuthenticationService authService = new AuthenticationServiceImpl(passwordEncoder, authenticationManager,
            userDetailsCustomService, jwtUtils, userRepository, mapper, roleRepository, accountService);
    private final IAccountService service = new AccountServiceImpl(accountRepository, accountMapper, authService);
    private final AccountController controller = new AccountController(service);

    private Account account;
    private User user;
    private LocalDateTime fecha;

    @BeforeEach
    void setUp() {
        fecha = LocalDateTime.now().plusMonths(-1);
        Set<Role> roles = new HashSet<>();

        roles.add(new Role(1L, "ADMIN", "Rol admin", LocalDateTime.now(), null));

        user = new User(1L, "admin", "admin", "admin@gmail.com",
                "$10$HqJv0Vzw0u6WzPss7JTTzuYazYo7qaQEjUBPkKSauSAlKeMyXt0Dm", LocalDateTime.now(),
                null, false, null, roles, null, null);

        account = new Account(1L, AccountCurrencyEnum.USD, 1500D, 3000D, fecha,
                null, false, user, null, null);
    }

    @Test
    void getAccountBalance() {
        Mockito.when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        String tokenGenerado = jwtUtils.generateToken(userDetailsCustomService.loadUserByUsername("admin@gmail.com"));

        Mockito.when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findAccountByUserId(1L)).thenReturn(List.of(account));

        ResponseEntity<List<AccountBalanceResponseDto>> response = controller.getAccountBalance("Bearer " + tokenGenerado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ResponseEntity<>(List.of(new AccountBalanceResponseDto(18.6, 3000.0, 3300.0)), HttpStatus.OK), response);

        account.setCurrency(AccountCurrencyEnum.ARS);
        Mockito.when(accountRepository.findAccountByUserId(1L)).thenReturn(List.of(account));
        assertEquals(HttpStatus.OK, controller.getAccountBalance("Bearer " + tokenGenerado).getStatusCode());
    }

    @Test
    void getAccountUserById() {
        Mockito.when(accountRepository.findAccountByUserId(1L)).thenReturn(List.of(account));

        ResponseEntity<List<AccountResponseDto>> response = controller.getAccountUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ResponseEntity<>(List.of(new AccountResponseDto(1L, "USD", 1500D, 3000D, 1L, fecha, null)), HttpStatus.OK), response);
    }
}*/
