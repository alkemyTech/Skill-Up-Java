package com.alkemy.wallet.service;


import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.dto.response.account.AccountBalanceDTO;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountBalanceService {

    //TODO create an interface to put/create/add the necessary methods and implement the interface. Rename this class as AccountServiceImpl
    private final IAccountRepository accountRepository;
    private final ITransactionRepository iTransactionRepository;

    //TODO as the user comes with the id in the parameters, use the AuthService and verify if the id is the same as the logged user
    public AccountBalanceDTO getAccountBalance(long idUser) {
        Optional<Account> account = accountRepository.findByFkUserId(idUser);
        if (account.isEmpty())
            return null;

        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
        if (account.get().getCurrency().equals(AccountCurrencyEnum.ARS)) {
            accountBalanceDTO.setBalanceDollars(account.get().getBalance() / 282);
            accountBalanceDTO.setBalancePesos(account.get().getBalance());
        }

        if (account.get().getCurrency().equals(AccountCurrencyEnum.USD)) {
            accountBalanceDTO.setBalancePesos(account.get().getBalance() * 282);
            accountBalanceDTO.setBalanceDollars(account.get().getBalance());
        }

        LocalDate dateDB = LocalDate.of
                (account.get().getCreationDate().getYear(),
                account.get().getCreationDate().getMonth(),
                account.get().getCreationDate().getDayOfWeek().getValue());

        Period duration = Period.between(dateDB, LocalDate.now());
        if (duration.getMonths() > 0){
            accountBalanceDTO.setPlazoFijo(account.get().getBalance() * (282 * duration.getMonths()));
        }

        return accountBalanceDTO;
    }


}
