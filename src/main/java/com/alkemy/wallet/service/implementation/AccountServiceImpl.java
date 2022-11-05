package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.TransactionLimitExceededException;
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
import org.springframework.beans.factory.annotation.Autowired;
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

        Account account = new Account(currency, transactionLimit, balance, user);

        return accountMapper.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto reduceBalance(int accountId, double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if(optionalAccount.isEmpty()){
            throw new InvalidParameterException("Not found account.");
        }

        Account updatedAccount = optionalAccount.get();

        double oldBalance = updatedAccount.getBalance();
        if(oldBalance < amount){
            throw new InvalidParameterException("The amount to reduce is bigger than the current balance.");
        }
        if(amount > updatedAccount.getTransactionLimit()) {
            throw new TransactionLimitExceededException("The balance reduction of " + amount + " exceeded the transaction limit of " + updatedAccount.getTransactionLimit());
        }

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
    @Override
    public AccountDto increaseBalance(int accountId, double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            throw new InvalidParameterException("Account with id " + accountId + " not found");
        }

        Account account = optionalAccount.get();
        double oldBalance = account.getBalance();

        if(amount <= 0){
            throw new InvalidAmountException();
        }
        if(amount > account.getTransactionLimit()){
            throw new TransactionLimitExceededException("The balance increase of " + amount + " exceeded the transaction limit of " + account.getTransactionLimit());
        }

        account.setBalance(oldBalance + amount);

        return accountMapper.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto getAccountById(Integer accountId) {
        Optional<Account> optionalAccountDto = accountRepository.findById(accountId);
        if(optionalAccountDto.isEmpty()){
            throw new ResourceNotFoundException("The account with id: " + accountId + " was not found");
        }

        return accountMapper.convertToDto(optionalAccountDto.get());
    }

    private double getTransactionLimitForCurrency(Currency currency){
        return switch (currency) {
            case ARS -> 300000;
            case USD -> 1000;
        };
    }
}
