package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IFixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;

@RequiredArgsConstructor
@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    private static final int daysClosingDate = 30;

    private static final double interestRate = 0.05;

    @Autowired
    BankDAO bankDAO;

    @Override
    public ResponseEntity<Object> saveFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit){
        if(!fixedTermDeposit.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !fixedTermDeposit.getCurrency().equalsIgnoreCase(USD.getCurrency()) ) {
            throw  new BankException("Currency not permitted");
        }
        LocalDate date = LocalDate.now();
        long days = date.until(fixedTermDeposit.getClosingDate(), ChronoUnit.DAYS);

        if(days < daysClosingDate) {
            throw new BankException("Fixed term deposit closing date must be greater than 30 days");
        }

        double interests = (fixedTermDeposit.getAmount() * interestRate) * days;
        fixedTermDeposit.setInterests(interests);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity entity = bankDAO.findUserByEmail(authentication.getName());
        Long id = entity.getUserId();

        AccountEntity account = bankDAO.getAccount(1L, fixedTermDeposit.getCurrency().toUpperCase());
        Optional<UserEntity> user = bankDAO.getUserById(1L);
        bankDAO.createFixedTermDeposit(fixedTermDeposit, account, user.orElseThrow(() -> new BankException("User does not exist DAO2")));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
