package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IFixedTermDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

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

        bankDAO.createFixedTermDeposit(fixedTermDeposit);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
