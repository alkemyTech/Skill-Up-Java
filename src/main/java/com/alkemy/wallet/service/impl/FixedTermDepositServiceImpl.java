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
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IFixedTermDepositService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.alkemy.wallet.utils.DateUtil.*;
import static com.alkemy.wallet.utils.FixedTermDepositUtil.calculateInterest;

@Service
@Transactional
@RequiredArgsConstructor
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    private final FixedTermDepositMapper mapper;
    private final IFixedTermDepositRepository repository;
    private final IAccountService accountService;
    private final IAuthService authService;
    private final IUserService userService;
    private final CustomMessageSource messageSource;

    @Override
    public FixedTermDepositResponseDto createNewFixedTermDeposit(FixedTermDepositRequestDto requestDto) {
        User user = userService.getUserByEmail(authService.getEmailFromContext());
        Account account = accountService.getAccountByCurrencyAndUserId(requestDto.getCurrency(), user.getId());

        if (!user.getAccounts().contains(account))
            throw new IllegalArgumentException(messageSource
                    .message("entity.out-of-bound", new String[] {"account"}));
        if (requestDto.getAmount() > account.getBalance())
            throw new IllegalArgumentException(messageSource.message("fixed.invalid-balance", null));

        long days = daysBetween2Dates(LocalDate.now(), string2LocalDate(requestDto.getClosingDate()));
        if (days < MIN_DAYS)
            throw new IllegalArgumentException(messageSource
                    .message("fixed.invalid-closing-date", new Integer[] {MIN_DAYS}));

        Double interest = calculateInterest(requestDto.getAmount(), days);
        Double newBalance = account.getBalance() - requestDto.getAmount();
        accountService.editBalanceAndSave(account, newBalance);

        FixedTermDeposit fixedTermDeposit = new FixedTermDeposit();
        fixedTermDeposit.setAmount(requestDto.getAmount());
        fixedTermDeposit.setInterest(interest);
        fixedTermDeposit.setClosingDate(string2LocalDate(requestDto.getClosingDate()));
        fixedTermDeposit.setAccount(account);
        fixedTermDeposit.setUser(user);

        return mapper.entity2Dto(repository.save(fixedTermDeposit));
    }

    @Override
    public FixedTermDepositSimulationResponseDto simulateDeposit(FixedTermDepositSimulateRequestDto request) {
        long days = daysBetween2Dates(LocalDate.now(), string2LocalDate(request.getClosingDate()));
        if (days < MIN_DAYS)
            throw new IllegalArgumentException(messageSource
                    .message("fixed.invalid-closing-date", new Integer[] {MIN_DAYS}));

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
