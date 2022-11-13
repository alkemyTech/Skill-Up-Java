package com.alkemy.wallet.controller;

import com.alkemy.wallet.auth.service.UserDetailsCustomService;
import com.alkemy.wallet.auth.utility.JwtUtils;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthControllerTest {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private final IUserRepository userRepository = Mockito.mock(IUserRepository.class);
    private final UserDetailsCustomService userDetailsCustomService = new UserDetailsCustomService(userRepository);
    private final JwtUtils jwtUtils = new JwtUtils();
    private final UserMapper mapper = new UserMapper();
    private final IRoleRepository roleRepository = Mockito.mock(IRoleRepository.class);
    private final IAccountService accountService = Mockito.mock(IAccountService.class);
    private final IAuthService service = new AuthServiceImpl(passwordEncoder, authenticationManager,
            userDetailsCustomService, jwtUtils, userRepository, mapper, roleRepository, accountService);

    private final AuthController controller = new AuthController(service);
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "ADMIN", "Rol admin", LocalDateTime.now(), null);
    }

    @Test
    void signUp() {
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        UserRequestDto userRequestDto = new UserRequestDto("admin", "admin", "admin@gmail.com",
                "12345", 1L);

        assertEquals(HttpStatus.CREATED, controller.signUp(userRequestDto).getStatusCode());
    }

    @Test
    void login() {
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("admin@gmail.com", "12345"))).
                thenReturn(new UsernamePasswordAuthenticationToken("admin@gmail.com", "12345"));

        Mockito.when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(new User(1L, "admin", "admin",
                "admin@gmail.com", "$10$HqJv0Vzw0u6WzPss7JTTzuYazYo7qaQEjUBPkKSauSAlKeMyXt0Dm", LocalDateTime.now(),
                null, false, null, Set.of(role), null, null)));

        assertEquals(HttpStatus.OK, controller.login(new AuthRequestDto("admin@gmail.com", "12345")).getStatusCode());
    }
}