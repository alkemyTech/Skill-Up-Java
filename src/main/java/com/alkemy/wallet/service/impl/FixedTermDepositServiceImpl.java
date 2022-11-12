package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDTO;
import com.alkemy.wallet.dto.validator.IValidatorFixedTermDep;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IFixedTermDepositService;
import com.alkemy.wallet.utils.DTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;

@RequiredArgsConstructor
@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {
    private static final int daysClosingDate = 30;
    private static final double interestRate = 0.005;
    @Autowired
    BankDAO bankDAO;

    @Override
    public ResponseEntity<Object> saveFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit){
        DTOValidator.validate(fixedTermDeposit, IValidatorFixedTermDep.class);
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
        double totalAmount = fixedTermDeposit.getAmount() + interests;
        fixedTermDeposit.setTotalAmount(totalAmount);
        fixedTermDeposit.setCreateDate(date);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        Long id = user.getUserId();
        AccountEntity account = bankDAO.getAccount(id, fixedTermDeposit.getCurrency().toUpperCase());
        account.setBalance(account.getBalance() - fixedTermDeposit.getAmount());
        bankDAO.createFixedTermDeposit(fixedTermDeposit, account, user);
        return new ResponseEntity<>("total amount: $"+ totalAmount, HttpStatus.CREATED);
    }
    @Override
    public FixedTermDepositSimulateDTO simulateDeposit(FixedTermDepositDTO fixedTermDeposit){
        DTOValidator.validate(fixedTermDeposit, IValidatorFixedTermDep.class);
        FixedTermDepositSimulateDTO fixedTermDepositSimulateDto= new FixedTermDepositSimulateDTO();
        if(!fixedTermDeposit.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !fixedTermDeposit.getCurrency().equalsIgnoreCase(USD.getCurrency()) ) {
            throw  new BankException("Currency not permitted");
        }
        LocalDate date = LocalDate.now();
        long days = date.until(fixedTermDeposit.getClosingDate(), ChronoUnit.DAYS);
        if(days < daysClosingDate) {
            throw new BankException("Fixed term deposit closing date must be greater than 30 days");
        }
        double interests = (fixedTermDeposit.getAmount() * interestRate) * days;
        double totalAmount = fixedTermDeposit.getAmount() + interests;
        fixedTermDepositSimulateDto.setCreateDate(date);
        fixedTermDepositSimulateDto.setAmount(fixedTermDeposit.getAmount());
        fixedTermDepositSimulateDto.setCurrency(fixedTermDeposit.getCurrency());
        fixedTermDepositSimulateDto.setClosingDate(fixedTermDeposit.getClosingDate());
        fixedTermDepositSimulateDto.setInterests(interests);
        fixedTermDepositSimulateDto.setTotalAmount(totalAmount);
        System.out.println("totalAmount = " + totalAmount);
        return fixedTermDepositSimulateDto;
        }
}

