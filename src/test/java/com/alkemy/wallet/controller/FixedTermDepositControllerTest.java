package com.alkemy.wallet.controller;

import com.alkemy.wallet.service.IFixedTermDepositService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(FixedTermDepositController.class)
class FixedTermDepositControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFixedTermDepositService service;

    @Test
    void save() {
    }
}