package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.response.fixed_term_deposit.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.mapper.request.IFixedTermDepositRequestMapper;
import com.alkemy.wallet.model.mapper.response.fixed_term_deposit.IFixedTermDepositResponseMapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FixedTermDepositService {
    private final IFixedTermDepositRequestMapper fixedTermDepositRequestMapper;
    private final IFixedTermDepositResponseMapper fixedTermDepositResponseMapper;
    private final IFixedTermDepositRepository fixedTermDepositRepository;
    private final AccountService accountService;

    public FixedTermDepositService(IFixedTermDepositRequestMapper fixedTermDepositRequestMapper,
                                   IFixedTermDepositRepository fixedTermDepositRepository,
                                   IFixedTermDepositResponseMapper fixedTermDepositResponseMapper,
                                   IAccountRepository accountRepository, AccountService accountService) {
        this.fixedTermDepositRequestMapper = fixedTermDepositRequestMapper;
        this.fixedTermDepositResponseMapper = fixedTermDepositResponseMapper;
        this.fixedTermDepositRepository = fixedTermDepositRepository;
        this.accountService = accountService;
    }

    public FixedTermDepositResponseDto save(FixedTermDepositRequestDto requestDto) {
        FixedTermDeposit fixedTermDeposit;
        fixedTermDeposit = fixedTermDepositRequestMapper.fixedTermDepositRequestToFixedTermDeposit(requestDto);

        Long ClosingDateDays = DAYS.between(LocalDateTime.now(),fixedTermDeposit.getClosingDate());

        //ToDo: Obtener la cuenta desde el token
        Long accountId=requestDto.getAccountId();
        Account account = accountService.findById(accountId).get() ;
        Double fixedDepositAmount = requestDto.getAmount();

        if ((ClosingDateDays >= 30) &&(account.getBalance()>=fixedDepositAmount)) {
            Double oldBalance = account.getBalance();
            account.setBalance(oldBalance - fixedDepositAmount);
            accountService.save(account);

            fixedTermDeposit.setCreationDate(LocalDateTime.now());

            Double interest = fixedDepositAmount;
            for (int i=0; i<ClosingDateDays; i++){
                interest = interest + interest* 0.005;
            }
            fixedTermDeposit.setInterest(interest);

            FixedTermDeposit fixedTermToSave = fixedTermDepositRepository.save(fixedTermDeposit);
            return fixedTermDepositResponseMapper.fixedTermDepositToFixedTermDepositResponse(fixedTermToSave);
        } else {
            throw new IllegalArgumentException(
                    String.format("Closing Date is less than 30 days: %s  or account has not enough money: %s",ClosingDateDays,fixedDepositAmount));
        }

    }
}
