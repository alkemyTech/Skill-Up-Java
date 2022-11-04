package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.FixedTermDepositEntity;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.service.IFixedTermDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {
    @Autowired
    FixedTermDepositRepository fixedTermDepositRepository;

    @Override
    public void saveFixedTermDeposit(FixedTermDepositEntity fixedTermDeposit){
        fixedTermDepositRepository.save(fixedTermDeposit);
    }
}
