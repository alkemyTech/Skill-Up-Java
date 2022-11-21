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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@Slf4j
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

        if (closingDateDays < 30)
            throw new IllegalArgumentException(String.format("Closing Date is less than 30 days: %s", closingDateDays));
        if (account.getBalance() < requestDto.getAmount())
            throw new IllegalArgumentException(String.format("Account has not enough money. Account: %s - To deposit: %s", account.getBalance(), requestDto.getAmount()));

        Double newBalance = account.getBalance() - requestDto.getAmount();
        accountService.editBalanceAndSave(account, newBalance);

        double interest = requestDto.getAmount();
        for (int i = 0; i < closingDateDays; i++) {
            interest = interest + interest * 0.005;
        }
        interest = interest - requestDto.getAmount();
        interest = Math.round(interest * 100d) / 100d;

        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder()
                .amount(requestDto.getAmount())
                .interest(interest)
                .creationDate(LocalDateTime.now())
                .closingDate(string2LocalDateTime(requestDto.getClosingDate()))
                .account(account)
                .user(user)
                .build();

        return mapper.entity2Dto(repository.save(fixedTermDeposit));
    }

    protected LocalDate string2LocalDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(dateTime, formatter);
    }
}
