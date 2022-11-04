package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountDTO createAccount(int userId, String currency) {
        // Encuentro la lista de cuentas con el UserId pasado por parametro
        List<Account> accountsByUserId = this.accountRepository.findByUser(userRepository.findById(userId).get());

        // Busco si alguna de las cuentas pertenecientes al UserId ya tiene la currency igual a la pasada por parametro
        boolean repeatedAccount = accountsByUserId.stream()
                .anyMatch(i -> currency.equals(i.getCurrency().name()));

        if(!repeatedAccount){
            Account accountEntity = new Account();
            accountEntity.setCurrency(CurrencyList.valueOf(currency));
            accountEntity.setCreationDate(Instant.now());
            accountEntity.setBalance(0.0);
            if (currency.equals("USD"))
                accountEntity.setTransactionLimit(1000.0);
            else
                accountEntity.setTransactionLimit(300000.0);

            accountEntity.setUpdateDate(Instant.now());
            accountEntity.setUser(userRepository.findById(userId).get());
            accountRepository.save(accountEntity);

        return accountMapper.convertToAccountDTO(accountEntity);

        } else {
            return  null;
        }
    }
}
