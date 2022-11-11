package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.model.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IFixedTermDepositService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {
    private final FixedTermDepositMapper mapper;
    private final IFixedTermDepositRepository fixedTermDepositRepository;
    private final IAccountService accountService;
    private  final IAuthService authService;

    public FixedTermDepositServiceImpl(FixedTermDepositMapper mapper,
                                       IFixedTermDepositRepository fixedTermDepositRepository,
                                       IAccountService accountService,
                                       IAuthService authService) {
       this.mapper = mapper;
        this.fixedTermDepositRepository = fixedTermDepositRepository;
        this.accountService = accountService;
        this.authService=authService;
    }

    @Override
    public FixedTermDepositResponseDto save(FixedTermDepositRequestDto requestDto, String token) {
        FixedTermDeposit fixedTermDeposit;
        fixedTermDeposit = mapper.dto2Entity(requestDto);

        Long closingDateDays = DAYS.between(LocalDateTime.now(),fixedTermDeposit.getClosingDate());

        User user = authService.getUserFromToken(token);
        Account fixedTermAccount = null;
        for(Account account: user.getAccounts()){
            if(account.getId() == requestDto.getAccountId()){
                fixedTermAccount = account;
            }
        }
        if(fixedTermAccount == null){
            throw new IllegalArgumentException(String.format("The accountId: %s is not valid o does not belong to an user account",requestDto.getAccountId()));
        }

        Double fixedDepositAmount = requestDto.getAmount();
        if ((closingDateDays >= 30) &&(fixedTermAccount.getBalance()>=fixedDepositAmount)) {
            Double newBalance=fixedTermAccount.getBalance() - fixedDepositAmount;
            accountService.editAccountBalance(requestDto.getAccountId(),newBalance);

            fixedTermDeposit.setCreationDate(LocalDateTime.now());

            Double interest = fixedDepositAmount;
            for (int i=0; i<closingDateDays; i++){
                interest = interest + interest* 0.005;
            }
            fixedTermDeposit.setInterest(interest);

            FixedTermDeposit fixedTermToSave = fixedTermDepositRepository.save(fixedTermDeposit);
            return mapper.entity2Dto(fixedTermToSave);
        } else {
            throw new IllegalArgumentException(
                    String.format("Closing Date is less than 30 days: %s  or account has not enough money: %s",closingDateDays,fixedDepositAmount));
        }

    }
}
