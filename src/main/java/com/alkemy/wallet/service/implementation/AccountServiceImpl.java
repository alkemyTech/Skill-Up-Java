package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.CurrencyRequestDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @Override
    public AccountDto createAccountByUserId(int userId, Currency currency) {
        User user = new User(userId);
        if (accountRepository.findAccountByUserIdAndCurrency(user, currency).isPresent()) {
            throw new RuntimeException("User already has an account for that currency.");
        }
        double balance = 0;
        double transactionLimit = getTransactionLimitForCurrency(currency);
        Date date = new Date();
        Timestamp creationDate = new Timestamp(date.getTime());
        Account account = new Account(user, currency, transactionLimit, balance, creationDate);
        return accountMapper.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto createAccountByUsername(String username, Currency currency) {
        /* Get userId from userService
        * */
        int userId = userService.getUserIdByEmail(username);
        User user = new User(userId);
        if (accountRepository.findAccountByUserIdAndCurrency(user, currency).isPresent()) {
            throw new RuntimeException("User already has an account for that currency.");
        }
        double balance = 0;
        double transactionLimit = getTransactionLimitForCurrency(currency);
        Date date = new Date();
        Timestamp creationDate = new Timestamp(date.getTime());
        Account account = new Account(user, currency, transactionLimit, balance, creationDate);
        return accountMapper.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto reduceBalance(int accountId, double amount) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Not found account.");
        }
        double oldBalance = account.get().getBalance();
        if (oldBalance < amount) {
            throw new InvalidParameterException("The amount to reduce is bigger than the current balance.");
        }
        Account updatedAccount = account.get();
        updatedAccount.setBalance(oldBalance - amount);
        return accountMapper.convertToDto(accountRepository.save(updatedAccount));
    }

    @Override
    public List<AccountDto> getAccountsByUserId(int userId) {
        User user = new User(userId);
        List<Account> accounts = accountRepository.findallByUserId(user);
        return accounts.stream().map(accountMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountByUserAndCurrency(int userId, CurrencyRequestDto currencyRequest) {
        User user = new User(userId);
        Optional<Account> optionalAccount = accountRepository.findAccountByUserIdAndCurrency(user, currencyRequest.currencyRequestToEnum());
        log.info("the account by user and currency is: " + optionalAccount);
        return null;
    }

    private double getTransactionLimitForCurrency(Currency currency) {
        return switch (currency) {
            case ARS -> 300000;
            case USD -> 1000;
        };
    }
}
