package com.alkemy.wallet.service;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.dao.EmptyResultDataAccessException;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void getAccountsByUserId(){

        //Arrange
        Account accountMock = new Account();
        accountMock.setId(1l);
        accountMock.setCurrency(Currency.usd);
        accountMock.setTransactionLimit(10.0);
        accountMock.setBalance(100.00);
        accountMock.setId(10l);
        accountMock.setCreationDate(new Date());
//        accountMock.setTimestamp(new Timestamp(System.currentTimeMillis()));
        accountMock.setSoftDelete(false);

        List<Account> simulations = new ArrayList<>();
        simulations.add(accountMock);

        when(accountRepository.findAllByUser_Id(10l))
                .thenReturn(simulations);

        //Act
        List<Account> accounts = accountService.getAccountsByUserId(10l);
        Integer count = accounts.size();

        //Assert
        assertEquals(count,1);

    }

    @Test
    @Disabled
    public void emptyResultDataAccessException(){
        //Arrange
        List<Account> simulations = new ArrayList<>();
        when(accountRepository.findAllByUser_Id(10l))
                .thenReturn(simulations);
        //Act
        String exceptionMessage = "";

        try {
            var accounts = accountService.getAccountsByUserId(10l);
        }catch (EmptyResultDataAccessException e){
            exceptionMessage  = e.getMessage();
        }

        //Assert
        assertThrows(EmptyResultDataAccessException.class, ()-> { accountService.getAccountsByUserId(10l);});
        assertEquals("El usuario no posee cuentas",  exceptionMessage);
    }

}