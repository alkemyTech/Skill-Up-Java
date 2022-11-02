package com.alkemy.wallet.service.implementation;

import ch.qos.logback.core.CoreConstants;
import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.service.FixedTermDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.concurrent.TimeUnit;

@Service
@Transactional()
public class FixedTermDepositServiceImpl implements FixedTermDepositService {
    @Autowired
    private FixedTermDepositRepository fixedTermDepositRepository;

    @Override
    public Integer createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto) {
        FixedTermDeposit fixedTermDeposit=new FixedTermDeposit();
        fixedTermDeposit.setAmount(fixedTermDepositDto.getAmount());
        fixedTermDeposit.setClosingDate(fixedTermDepositDto.getClosingDate());
        Timestamp timestamp=new Timestamp(new Date().getTime());
        fixedTermDeposit.setCreationDate(timestamp);
        System.out.println(fixedTermDeposit.getCreationDate());
        System.out.println(fixedTermDeposit.getClosingDate());

        Long days= ((fixedTermDeposit.getClosingDate().getTime())-fixedTermDeposit.getCreationDate().getTime())/86400000 ; //No anda
        System.out.println(days);
           if(days<30){
               System.out.println("the fixed plan entered, it has less than 30 days");
               return 0;
           }


        fixedTermDeposit.setInterest(0.5*days);

        fixedTermDepositRepository.save(fixedTermDeposit);
        return fixedTermDeposit.getFixedTermDepositId();
    }
}
