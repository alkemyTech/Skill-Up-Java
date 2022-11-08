package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.entity.FixedTermDepositEntity;
import com.alkemy.wallet.entity.UserEntity;
import com.alkemy.wallet.mapper.exception.MinDaysException;
import com.alkemy.wallet.mapper.FixedTermDepositMap;
import com.alkemy.wallet.repository.IFixedTermDepositRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IFixedTermDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FixedTermServiceImpl implements IFixedTermDepositService {


    @Autowired
    private FixedTermDepositMap fixedTermDepositMap;
    @Autowired
    private IFixedTermDepositRepository fixedTermDepositRepository;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public FixedTermDepositDto createNewFixedTerm(FixedTermDepositDto dto) {

        FixedTermDepositEntity entity = fixedTermDepositMap.fixedTermDepositDto2Entity(dto);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        entity.setUserId(user.getUserId());

        entity.setAccountId(20L);

        entity.setCreationDate(new Date());

        Long minDays = ((entity.getCreationDate().getTime() - entity.getClosingDate().getTime())
                / 86400000);
        if (minDays > 30) {
            throw new MinDaysException("The Closing date cannot be less than 30 days.");
        }

        entity.setInterest((minDays * 0.5)*-1);

        Long id = 20L;

        accountService.updateBalance(id, entity.getAmount());
        FixedTermDepositEntity CreatedFixedDeposit = fixedTermDepositRepository.save(entity);
        return fixedTermDepositMap.fixedTermDepositEntity2Dto(CreatedFixedDeposit);
    }
}

