package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.LoginUserDto;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.util.DataLoaderUser;
import com.alkemy.wallet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import({ObjectMapper.class, AuthController.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    public IUserRepository userRepo;
    @Autowired
    public IRoleRepository roleRepo;
    RequestUserDto requestUserDto;

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    DataLoaderUser dataLoaderUser;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Signing up success")
    @Test
    @Order(2)
    void signUpSuccess() throws Exception {

        requestUserDto = RequestUserDto.builder()
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .password("test").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(status().isCreated()).andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("test")))
                .andExpect(jsonPath("$.lastName", is("test")))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.token", not(Matchers.blankString())));
    }

    @DisplayName("Signing up failed notNull nulled")
    @Test
    @Order(1)
    void signUpFail() throws Exception {

        requestUserDto = RequestUserDto.builder()
                .firstName("test")
                .lastName(null)
                .email("test@test.com")
                .password("test").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Signing in success")
    @Test
    @Order(4)
    void signInSuccess() throws Exception {

        LoginUserDto loginDto = LoginUserDto.builder()
                .email("test@test.com")
                .password("test").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }

    @DisplayName("Signing in failed wrong pass")
    @Test
    @Order(3)
    void signInFailed() throws Exception {
        LoginUserDto loginDto = LoginUserDto.builder()
                .email("test@test.com")
                .password("cualquiercosa").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isForbidden());
    }
}