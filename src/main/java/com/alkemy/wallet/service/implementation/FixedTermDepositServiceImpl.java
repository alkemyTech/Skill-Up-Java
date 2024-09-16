package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDto;
import com.alkemy.wallet.exception.FixedTermDepositException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.FixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
    private final AccountRepository accountRepository;

    @Override
    public FixedTermDepositDto createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto, String token) throws FixedTermDepositException {
        FixedTermDeposit fixedTermDeposit=mapper.convertToEntity(fixedTermDepositDto);
        Timestamp timestamp=new Timestamp(new Date().getTime());
        fixedTermDeposit.setCreationDate(timestamp);

        Long days= ((fixedTermDeposit.getClosingDate().getTime())-fixedTermDeposit.getCreationDate().getTime())/86400000 ;
        if(days<30){
            throw new FixedTermDepositException();
        }
        fixedTermDeposit.setAccount(accountRepository.findAccountByUserIdAndCurrency(userService.loadUserByUsername(jwtUtil.extractClaimUsername(token.substring(7))),fixedTermDepositDto.getCurrency()).get());
        fixedTermDeposit.setInterest(0.5*days);
        fixedTermDepositRepository.save(fixedTermDeposit);
        return fixedTermDepositDto;

    }

    @Override
    public List<FixedTermDepositDto> getAccountFixedTermDeposits(int accountId) {
        Account account = new Account(accountId);
        return fixedTermDepositRepository.findallByAccount(account).stream()
                .map(mapper::convertToDto).collect(Collectors.toList());
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
