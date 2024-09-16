package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.TransactionLimitExceededException;
import com.alkemy.wallet.dto.CurrencyRequestDto;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.FixedTermDepositService;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final JWTUtil jwtUtil;
    private final AccountMapper accountMapper;
    private final UserService userService;
    private final FixedTermDepositService fixedTermDepositService;

    @Override
    public AccountDto createAccountByUserId(int userId, Currency currency) {
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
    public AccountDto createAccount(String token, CurrencyRequestDto currencyDto) {
        String username = jwtUtil.extractClaimUsername(token.substring(7));
        User user = userService.loadUserByUsername(username);
        Currency currency = currencyDto.currencyRequestToEnum();
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
            throw new ResourceNotFoundException("Not found account.");
        }

        Account updatedAccount = optionalAccount.get();

        double oldBalance = updatedAccount.getBalance();
        if(oldBalance < amount){
            throw new InvalidAmountException("The amount to reduce is bigger than the current balance.");
        }
        if(amount > updatedAccount.getTransactionLimit()) {
            throw new TransactionLimitExceededException("The balance reduction of " + amount + " exceeded the transaction limit of " + updatedAccount.getTransactionLimit());
        }

        updatedAccount.setBalance(oldBalance - amount);
        return accountMapper.convertToDto(accountRepository.save(updatedAccount));
    }


    public Account findAccountByUserIdAndCurrency(User user, Currency currency){
        Optional<Account> optionalAccount = accountRepository.findAccountByUserIdAndCurrency(user, currency);
        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("The account couldn't be found.");
        }
        return optionalAccount.get();
    }

    @Override
    public List<AccountDto> getAccountsByUserId(int userId) {
        User user = new User(userId);
        List<Account> accounts = accountRepository.findAccountsByUserId(user);
        return accounts.stream().map(accountMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<AccountBalanceDto> getUserBalance(String token) {
        String username = jwtUtil.extractClaimUsername(token.substring(7));
        User user = userService.loadUserByUsername(username);
        List<AccountDto> userAccounts = getAccountsByUserId(user.getUserId());
        return userAccounts
                .stream()
                .map(this::getAccountBalance)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDetailDto updateAccount(AccountPatchDto accountPatch, Integer Id, String userToken) throws ResourceNotFoundException {
        var account = accountRepository.findById(Id);
        if (account.isPresent()){
            userService.matchUserToToken(account.get().getUser().getUserId(),userToken);
            account.get().setTransactionLimit(accountPatch.transactionLimit());
            return accountMapper.convertToAccountDetailDto(accountRepository.save(account.get()));
        }else{
            throw new ResourceNotFoundException("Account does not exist");
        }
    }

    @Override
    public boolean hasUserAccountById(Integer userId, Integer accountId) {
        AccountDto accountDto = getAccountById(accountId);

        return accountDto.userId().equals(userId);
    }

    private AccountBalanceDto getAccountBalance(AccountDto account) {
        AccountBalanceDto accountBalance = accountMapper.convertAccountDtoToAccountBalanceDto(account);
        accountBalance.setFixedTermDeposits(fixedTermDepositService.getAccountFixedTermDeposits(account.id()));
        return accountBalance;
    }

    @Override
    public AccountDto increaseBalance(int accountId, double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()){
            throw new InvalidParameterException("Account with id " + accountId + " not found");
        }

        Account account = optionalAccount.get();
        double oldBalance = account.getBalance();

        if(amount <= 0){
            throw new InvalidAmountException("The amount must be greater than 0");
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
