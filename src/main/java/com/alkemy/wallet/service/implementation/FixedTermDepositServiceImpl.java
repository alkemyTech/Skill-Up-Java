package com.alkemy.wallet.service.implementation;

import ch.qos.logback.core.CoreConstants;
import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDto;
import com.alkemy.wallet.exception.FixedTermDepositException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.FixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional()
@RequiredArgsConstructor
public class FixedTermDepositServiceImpl implements FixedTermDepositService {
    @Autowired
    private FixedTermDepositRepository fixedTermDepositRepository;
    private final FixedTermDepositMapper mapper;
    private final AccountMapper accountMapper;
    private final JWTUtil jwtUtil;
    private final UserServiceImpl userService;
    private final AccountService accountService;





    @Override
    public FixedTermDepositDto createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto, String token) throws FixedTermDepositException {
        fixedTermDepositDto.setClosingDate(new Timestamp(fixedTermDepositDto.getClosingDate().getTime()+86400000));
        FixedTermDeposit fixedTermDeposit = mapper.convertToEntity(fixedTermDepositDto);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        fixedTermDeposit.setCreationDate(timestamp);


        Long days = (((fixedTermDeposit.getClosingDate().getTime()) - fixedTermDeposit.getCreationDate().getTime()) / 86400000);
        if (days < 30) {
            throw new FixedTermDepositException();
        }
        User u = userService.loadUserByUsername(jwtUtil.extractClaimUsername(token.substring(7)));
        Currency currency = fixedTermDepositDto.getCurrency();
        Account account=accountService.findAccountByUserIdAndCurrency(u,fixedTermDepositDto.getCurrency());
        fixedTermDeposit.setAccount(account);
        fixedTermDeposit.setInterest(0.5*days);
        accountService.reduceBalance(account.getAccountId(),fixedTermDepositDto.getAmount());
        fixedTermDepositRepository.save(fixedTermDeposit);
        return fixedTermDepositDto;

    }


    @Override
    public FixedTermDepositSimulateDto simulateFixedTermDepositDto(FixedTermDepositDto fixedTermDepositDto) {
        FixedTermDepositSimulateDto fixedTermDepositSimulateDto=new FixedTermDepositSimulateDto();
        Timestamp timestamp=new Timestamp(new Date().getTime());
        fixedTermDepositSimulateDto.setCreationDate(timestamp);
        fixedTermDepositSimulateDto.setClosingDate(fixedTermDepositDto.getClosingDate());
        Long days= ((fixedTermDepositSimulateDto.getClosingDate().getTime())-fixedTermDepositSimulateDto.getCreationDate().getTime())/86400000 ;
        if(days<30){
            throw new FixedTermDepositException();
        }
        fixedTermDepositSimulateDto.setCurrency(fixedTermDepositDto.getCurrency());
        fixedTermDepositSimulateDto.setInterest(0.5*days);
        fixedTermDepositSimulateDto.setAmount(fixedTermDepositDto.getAmount());
        fixedTermDepositSimulateDto.setTotalAmount(fixedTermDepositDto.getAmount()+((fixedTermDepositDto.getAmount()*fixedTermDepositSimulateDto.getInterest())/100));

        return fixedTermDepositSimulateDto;
    }
}
