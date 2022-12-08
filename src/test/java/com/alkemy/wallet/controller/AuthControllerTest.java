package com.alkemy.wallet.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import com.alkemy.wallet.config.SecurityConfig;
import com.alkemy.wallet.dto.LoginUserDto;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

@WebMvcTest(value = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private IUserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ObjectMapper objectMapper;

    User user;

    Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .description("Usuario")
                .name(RoleName.ROLE_USER)
                .creationDate(new Date())
                .build();

        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("test")
                .lastName("test")
                .password(passwordEncoder.encode("test"))
                .role(role)
                .softDelete(false)
                .creationDate(new Date())
                .updateDate(new Date())
                .build();

    }


    @DisplayName("Signing up success")
    @Test
    void signUpSucces() throws Exception {
        // given
        ResponseUserDto userDto = ResponseUserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();


        when(customUserDetailsService.save(any(RequestUserDto.class)))
                .thenReturn(userDto);


        mockMvc.perform(post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "test@test.com",
                                    "firstName": "test",
                                    "lastName": "test",
                                    "password": "test"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Signing up failed notNull nulled")
    @Test
    void signUpFail() throws Exception {
        // given
        RequestUserDto user = RequestUserDto.builder()
                .email("laacade@gmail.com")
                .firstName(null)
                .lastName("Argento")
                .password("racing4ever")
                .build();

        // when

        ResultActions response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
        // then
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Signing in success")
    @Test
    void signInSuccess() throws Exception {
        // given
        RequestUserDto user = RequestUserDto.builder()
                .email("laacade@gmail.com")
                .firstName("Pepe")
                .lastName("Argento")
                .password("racing4ever")
                .build();

        ResultActions verifyGiven = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        verifyGiven.andDo(print())
                .andExpect(status().isCreated());


        // when
        ResultActions result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "asd",
                                    "password": "asd"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Signing in failed wrong pass")
    @Test
    void signInFailed() throws Exception {

        ResultActions result = mockMvc.perform(post("/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors
                                .authentication(new UsernamePasswordAuthenticationToken(
                                        "laacade@gmail.com", "otracosa")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}