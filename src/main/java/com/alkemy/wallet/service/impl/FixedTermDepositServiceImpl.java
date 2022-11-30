package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.request.FixedTermDepositSimulateRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositSimulationResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.IFixedTermDepositService;
import com.alkemy.wallet.service.IUserService;
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

    protected static final double INTEREST_RATE = 0.05;
    protected static final int MIN_DAYS = 30;

    private final FixedTermDepositMapper mapper;
    private final IFixedTermDepositRepository repository;
    private final IAccountService accountService;
    private final IAuthenticationService authService;
    private final IUserService userService;

    @Override
    public FixedTermDepositResponseDto create(FixedTermDepositRequestDto requestDto) {
        User user = userService.getByEmail(authService.getEmailFromContext());
        Account account = accountService.getByCurrencyAndUserId(requestDto.getCurrency(), user.getId());

        if (!user.getAccounts().contains(account))
            throw new IllegalArgumentException(
                    String.format("The account with id %s does not belong to the current user",
                            account.getId()));
        if (requestDto.getAmount() > account.getBalance())
            throw new IllegalArgumentException("Not enough money to deposit in fixed term");

        long days = daysBetween2Dates(LocalDate.now(), string2LocalDate(requestDto.getClosingDate()));
        if (days < MIN_DAYS)
            throw new IllegalArgumentException(
                    String.format("Closing Date is less than %s days: %s", MIN_DAYS, days));

        Double interest = calculateInterest(requestDto.getAmount(), days);
        Double newBalance = account.getBalance() - requestDto.getAmount();
        accountService.editBalanceAndSave(account, newBalance);

        FixedTermDeposit fixedTermDeposit = FixedTermDeposit.builder()
                .amount(requestDto.getAmount())
                .interest(interest)
                .creationDate(LocalDateTime.now())
                .closingDate(string2LocalDate(requestDto.getClosingDate()))
                .account(account)
                .user(user)
                .build();

        return mapper.entity2Dto(repository.save(fixedTermDeposit));
    }

    @Override
    public FixedTermDepositSimulationResponseDto simulateDeposit(FixedTermDepositSimulateRequestDto request) {
        long days = daysBetween2Dates(LocalDate.now(), string2LocalDate(request.getClosingDate()));
        if (days < MIN_DAYS)
            throw new IllegalArgumentException(
                    String.format("Closing Date is less than %s days: %s", MIN_DAYS, days));

        Double interest = calculateInterest(request.getAmount(), days);
        return FixedTermDepositSimulationResponseDto.builder()
                .createdAt(LocalDate.now())
                .closingDate(string2LocalDate(request.getClosingDate()))
                .amountInvested(request.getAmount())
                .interestEarned(interest)
                .totalEarned(request.getAmount() + interest)
                .build();
    }

    protected LocalDate string2LocalDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(dateTime, formatter);
    }

    protected Double calculateInterest(Double amount, Long days) {
        double interest = 0;
        for (int i = 0; i < days; i++) {
            interest = interest + amount * INTEREST_RATE;
        }
        return interest;
    }

    protected Long daysBetween2Dates(LocalDate date1, LocalDate date2) {
        return DAYS.between(date1, date2);
    }
}
