package com.alkemy.wallet;

import com.alkemy.wallet.configuration.JwtUtil;
import com.alkemy.wallet.dto.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.alkemy.wallet.model.TypeEnum.DEPOSIT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransactionsDepositTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Before
    public void setup() {
        UserDetails dummy = new User("foo@foo.com", "foo", new ArrayList<>());
        Mockito.when(jwtUtil.generateToken(dummy)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb29AZm9vLmNvbSIsImV4cCI6MTY2Nzk2NjQ0MSwiaWF0IjoxNjY3OTMwNDQxfQ.hJ78zEJZ6a1t0E59CotdI1lPMobP7353mppZk4sskxM");
    }
    @Test
    public void transactionsDepositReturns201HttpCreated() throws Exception {
        UserDetails dummy = new User("foo@foo.com", "foo", new ArrayList<>());
        String jwtToken = jwtUtil.generateToken(dummy);
        String json = objectMapper.writeValueAsString(new TransactionDTO(1L, DEPOSIT.getType(), "deposito", 3000.0, "ARS", LocalDateTime.now()));
        mvc.perform(MockMvcRequestBuilders
                        .post("/transactions/deposit")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}
