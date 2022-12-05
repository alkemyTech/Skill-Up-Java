package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDto;
import com.alkemy.wallet.dto.SimulatedFixedTermDto;
import com.alkemy.wallet.exception.FixedTermException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IFixedTermRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IFixedTermService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class FixedTermService implements IFixedTermService {

    private final Mapper mapper;

    private final IUserRepository userRepository;

    private final IFixedTermRepository fixedTermRepository;

    private final IAccountRepository accountRepository;

    private static final Integer MIN_DAYS = 30;

    private static final Double DAILY_INTEREST = 0.005;

    public FixedTermService(Mapper mapper, IUserRepository userRepository, IFixedTermRepository fixedTermRepository, IAccountRepository accountRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.fixedTermRepository = fixedTermRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public FixedTermDto createFixedTerm(FixedTermDto fixedTermDto) {
        FixedTermDeposit fixedTerm = mapper.getMapper().map(fixedTermDto, FixedTermDeposit.class);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        Account account = accountRepository.findByCurrencyAndUser_id(fixedTermDto.getCurrency(), user.getId());

        fixedTerm.setAccount(account);
        fixedTerm.setCreationDate(LocalDate.now());

        Integer days = Period.between(fixedTerm.getCreationDate(), fixedTerm.getClosingDate()).getDays();

        if (days < MIN_DAYS) {
            throw new FixedTermException("Closing Date must be greater or equal to " + MIN_DAYS + "days");
        }

        fixedTerm.setInterest(fixedTerm.getAmount() * DAILY_INTEREST * days);
        account.setBalance(account.getBalance() - fixedTerm.getAmount());

        FixedTermDeposit fixedTermSaved = fixedTermRepository.save(fixedTerm);

        return mapper.getMapper().map(fixedTerm, FixedTermDto.class);

    }

    @Override
    public SimulatedFixedTermDto simulateFixedTerm(SimulatedFixedTermDto fixedTermDto) {

        Integer days = Period.between(fixedTermDto.getCreationDate(), fixedTermDto.getClosingDate()).getDays();

        if (days < MIN_DAYS) {
            throw new FixedTermException("Closing Date must be greater or equal to " + MIN_DAYS + "days");
        }

        Double interest = fixedTermDto.getAmount() * DAILY_INTEREST * days;

        fixedTermDto.setInterest(interest);

        fixedTermDto.setTotalAmount(fixedTermDto.getAmount() + interest);

        return fixedTermDto;
    }
}
