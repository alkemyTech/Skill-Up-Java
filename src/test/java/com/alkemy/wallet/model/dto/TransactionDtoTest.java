/*
package com.alkemy.wallet.model.dto;

import com.alkemy.wallet.model.TransactionDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class TransactionDtoTest {
    private ModelMapper mapper;
    private Transaction transaction;
    private User user;
    private Account account;

    @Before
    public void setUp() {
        mapper = new ModelMapper();
        transaction = new Transaction();
        user = new User();
        account = new Account();
    }

    @Test
    public void convertTransactionEntityToDto() {
        user.setId(13L);
        account.setId(456L);

        transaction.setId(1L);
        transaction.setAmount(124.0);
        transaction.setType(TransactionTypeEnum.PAYMENT);
        transaction.setDescription("Pago de servicio");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setAccount(account);

        TransactionDto dto = mapper.map(transaction, TransactionDto.class);

        assertEquals(transaction.getId(), dto.getId());
        assertEquals(transaction.getAmount(), dto.getAmount());
        assertEquals(transaction.getType(), dto.getType());
        assertEquals(transaction.getDescription(), dto.getDescription());
        assertEquals(transaction.getTransactionDate(), dto.getTransactionDate());
        assertEquals(transaction.getTransactionDate(), dto.getTransactionDate());
        assertEquals(transaction.getUser().getId(), dto.getUser().getId());
        assertEquals(transaction.getAccount().getId(), dto.getAccount().getId());

    }
}
*/
