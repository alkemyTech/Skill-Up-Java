package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IFixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    private final FixedTermDepositMapper mapper;
    private final IFixedTermDepositRepository repository;
    private final IAccountService accountService;
    private final IAuthService authService;

    @Override
    public FixedTermDepositResponseDto create(FixedTermDepositRequestDto requestDto, String token) {
        User user = authService.getUserFromToken(token);
        Account account = accountService.getAccountById(requestDto.getAccountId());

        if (!user.getAccounts().contains(account))
            throw new IllegalArgumentException(String.format("The account with id %s does not belong to the current user", requestDto.getAccountId()));

        long closingDateDays = DAYS.between(LocalDate.now(), string2LocalDateTime(requestDto.getClosingDate()));

        Double fixedDepositAmount = requestDto.getAmount();
        if ((closingDateDays < 30) && (account.getBalance() < fixedDepositAmount))
            throw new IllegalArgumentException(String.format("Closing Date is less than 30 days: %s  or account has not enough money: %s", closingDateDays, fixedDepositAmount));

        Double newBalance = account.getBalance() - fixedDepositAmount;
        accountService.editBalanceAndSave(account, newBalance);

        double interest = fixedDepositAmount;
        for (int i = 0; i < closingDateDays; i++) {
            interest = interest + interest * 0.005;
        }

        FixedTermDeposit fixedTermDeposit = mapper.dto2Entity(requestDto, interest, string2LocalDateTime(requestDto.getClosingDate()),account, user);
        return mapper.entity2Dto(repository.save(fixedTermDeposit));
    }

    protected LocalDate string2LocalDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(dateTime, formatter);
    }
}
