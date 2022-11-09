package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.model.request.AccountRequestDto;
import com.alkemy.wallet.model.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.response.AccountBalanceDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository iTransactionRepository;

    @Override
    public AccountResponseDto save(AccountRequestDto request) {
        return null;
    }

    //TODO as the user comes with the id in the parameters, use the AuthService and verify if the id is the same as the logged user
    public AccountBalanceDto getAccountBalance(long idUser) {
        Optional<Account> account = accountRepository.findByUserId(idUser);
        if (account.isEmpty())
            return null;

        AccountBalanceDto accountBalanceDTO = new AccountBalanceDto();
        if (account.get().getCurrency().equals(AccountCurrencyEnum.ARS)) {
            accountBalanceDTO.setBalanceUSD(account.get().getBalance() / 282);
            accountBalanceDTO.setBalanceARS(account.get().getBalance());
        }

        if (account.get().getCurrency().equals(AccountCurrencyEnum.USD)) {
            accountBalanceDTO.setBalanceUSD(account.get().getBalance() * 282);
            accountBalanceDTO.setBalanceARS(account.get().getBalance());
        }

        LocalDate dateDB = LocalDate.of
                (account.get().getCreationDate().getYear(),
                account.get().getCreationDate().getMonth(),
                account.get().getCreationDate().getDayOfWeek().getValue());

        Period duration = Period.between(dateDB, LocalDate.now());
        if (duration.getMonths() > 0){
            accountBalanceDTO.setFixedTermDeposit(account.get().getBalance() * (282 * duration.getMonths()));
        }

        return accountBalanceDTO;
    }


}
