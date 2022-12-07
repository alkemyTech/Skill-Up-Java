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
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.alkemy.wallet.utils.DateUtil.*;
import static com.alkemy.wallet.utils.FixedTermDepositUtil.calculateInterest;

@Service
@Transactional
@RequiredArgsConstructor
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    private final FixedTermDepositMapper mapper;
    private final IFixedTermDepositRepository repository;
    private final IAccountService accountService;
    private final IAuthenticationService authService;
    private final IUserService userService;
    private final CustomMessageSource messageSource;

    @Override
    public FixedTermDepositResponseDto create(FixedTermDepositRequestDto requestDto) {
        User user = userService.getByEmail(authService.getEmailFromContext());
        Account account = accountService.getByCurrencyAndUserId(requestDto.getCurrency(), user.getId());

        if (!user.getAccounts().contains(account))
            throw new IllegalArgumentException(messageSource
                    .message("fixed.out-of-bound", new Long[] {account.getId()}));
        if (requestDto.getAmount() > account.getBalance())
            throw new IllegalArgumentException(messageSource.message("fixed.no-balance", null));

        long days = daysBetween2Dates(LocalDate.now(), string2LocalDate(requestDto.getClosingDate()));
        if (days < MIN_DAYS)
            throw new IllegalArgumentException(messageSource
                    .message("fixed.closing-days", new Integer[] {MIN_DAYS}));

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
            throw new IllegalArgumentException(messageSource
                    .message("fixed.closing-days", new Integer[] {MIN_DAYS}));

        Double interest = calculateInterest(request.getAmount(), days);
        return FixedTermDepositSimulationResponseDto.builder()
                .createdAt(LocalDate.now())
                .closingDate(string2LocalDate(request.getClosingDate()))
                .amountInvested(request.getAmount())
                .interestEarned(interest)
                .totalEarned(request.getAmount() + interest)
                .build();
    }
}
