package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import({ObjectMapper.class, UserController.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser
    @Order(1)
    void updateUser() throws Exception {
        RequestUserDto requestUserDto = RequestUserDto.builder()
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .password("test").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(status().isCreated());

        RequestUserDto updatedUser = RequestUserDto.builder()
                .firstName("updatedTest")
                .lastName("test")
                .email("test@test.com")
                .password("test").build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is(updatedUser.getFirstName())));
    }

    @Test
    @WithMockUser(username="test@test.com",password="test")
    @Order(2)
    void getUserLoggedDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("updatedTest")));
    }
}