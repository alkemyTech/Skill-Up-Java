package com.alkemy.wallet.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import com.alkemy.wallet.config.SecurityConfig;
import com.alkemy.wallet.dto.LoginUserDto;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("Signing up success")
    @Test
    void signUpSucces() throws Exception {
        // given
        RequestUserDto user = RequestUserDto.builder()
                .email("laacade@gmail.com")
                .firstName("Pepe")
                .lastName("Argento")
                .password("racing4ever")
                .build();

//        // when
//        ResultActions response = mockMvc.perform(post("/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(user)));
//        // then
//        response.andDo(print())
//                .andExpect(status().isCreated());


        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
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

        LoginUserDto userLogin = LoginUserDto.builder()
                .email("laacade@gmail.com")
                .password("racing4ever")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLogin)));
        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Signing in failed wrong pass")
    @Test
    void signInFailed() throws Exception {
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

        LoginUserDto userLogin = LoginUserDto.builder()
                .email("laacade@gmail.com")
                .password("otracosa")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isForbidden());
    }
}