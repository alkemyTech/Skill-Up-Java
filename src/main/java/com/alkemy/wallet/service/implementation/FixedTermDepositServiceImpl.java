package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.service.FixedTermDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional()
public class FixedTermDepositServiceImpl implements FixedTermDepositService {
    @Autowired
    private FixedTermDepositRepository fixedTermDepositRepository;

    @Override
    public Integer createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto) {
        FixedTermDeposit fixedTermDeposit=new FixedTermDeposit();
        fixedTermDeposit.setAmount(fixedTermDepositDto.getAmount());
        fixedTermDeposit.setInterest(fixedTermDepositDto.getInterest());
        fixedTermDeposit.setClosingDate(fixedTermDepositDto.getClosingDate());
        Timestamp timestamp=new Timestamp(new Date().getTime());
        fixedTermDeposit.setCreationDate(timestamp);


        fixedTermDepositRepository.save(fixedTermDeposit);
        return fixedTermDeposit.getFixedTermDepositId();
    }
}
