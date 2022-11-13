package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.*;
import com.alkemy.wallet.dto.CurrencyRequestDto;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.model.*;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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
    private final FixedTermDepositRepository fixedTermDepositRepository;

    private final FixedTermDepositMapper fixedTermDepositMapper;
    @Autowired
    private HttpServletRequest request;

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
            throw new UserAlreadyHasAccountException("User already has an account for that currency");
        }
        double balance = 0;
        double transactionLimit = getTransactionLimitForCurrency(currency);

        Account account = new Account(currency, transactionLimit, balance, user);

        return accountMapper.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto reduceBalance(int accountId, double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("Not found account.");
        }

        Account updatedAccount = optionalAccount.get();

        double oldBalance = updatedAccount.getBalance();
        if (oldBalance < amount) {
            throw new InvalidAmountException("The amount to reduce is bigger than the current balance.");
        }
        if (amount > updatedAccount.getTransactionLimit()) {
            throw new TransactionLimitExceededException("The balance reduction of " + amount + " exceeded the transaction limit of " + updatedAccount.getTransactionLimit());
        }

        updatedAccount.setBalance(oldBalance - amount);
        return accountMapper.convertToDto(accountRepository.save(updatedAccount));
    }


    public Account findAccountByUserIdAndCurrency(User user, Currency currency) {
        Optional<Account> optionalAccount = accountRepository.findAccountByUserIdAndCurrency(user, currency);
        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("The account couldn't be found.");
        }
        return optionalAccount.get();
    }

    @Override
    public List<AccountDto> getAccountsByUserId(int userId, String userToken) {
        User user = userService.loadUserByUsername(jwtUtil.extractClaimUsername(userToken.substring(7)));
        List<Account> accounts = accountRepository.findAccountsByUserId(userService.getUserById(userId));
        return accounts.stream().map(accountMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public List<AccountBalanceDto> getUserBalance(String token) {
        String username = jwtUtil.extractClaimUsername(token.substring(7));
        User user = userService.loadUserByUsername(username);
        List<AccountDto> userAccounts = getAccountsByUserId(user.getUserId(), token);
        return userAccounts
                .stream()
                .map(this::getAccountBalance)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDetailDto updateAccount(AccountPatchDto accountPatch, Integer Id, String userToken) throws ResourceNotFoundException {
        var account = accountRepository.findById(Id);
        if (account.isPresent()) {
            userService.matchUserToToken(account.get().getUser().getUserId(), userToken);
            account.get().setTransactionLimit(accountPatch.transactionLimit());
            return accountMapper.convertToAccountDetailDto(accountRepository.save(account.get()));
        } else {
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
        List<FixedTermDeposit> f = fixedTermDepositRepository.findByAccount_AccountId(account.id());
        List<FixedTermDepositDto> fDto = new ArrayList<>();
        for (FixedTermDeposit a : f) {
            fDto.add(fixedTermDepositMapper.convertToDto(a));
        }
        accountBalance.setFixedTermDeposits(fDto);
        return accountBalance;
    }

    @Override
    public AccountDto increaseBalance(int accountId, double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new InvalidParameterException("Account with id " + accountId + " not found");
        }

        Account account = optionalAccount.get();
        double oldBalance = account.getBalance();

        if (amount <= 0) {
            throw new InvalidAmountException("The amount must be greater than 0");
        }
        if (amount > account.getTransactionLimit()) {
            throw new TransactionLimitExceededException("The balance increase of " + amount + " exceeded the transaction limit of " + account.getTransactionLimit());
        }

        account.setBalance(oldBalance + amount);

        return accountMapper.convertToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto getAccountById(Integer accountId) {
        Optional<Account> optionalAccountDto = accountRepository.findById(accountId);
        if (optionalAccountDto.isEmpty()) {
            throw new ResourceNotFoundException("The account with id: " + accountId + " was not found");
        }

        return accountMapper.convertToDto(optionalAccountDto.get());
    }

    @Override
    public PaginatedAccountsDto getPaginatedAccountsByUserId(int userId, int page, String userToken) {
        User user = userService.loadUserByUsername(jwtUtil.extractClaimUsername(userToken.substring(7)));
        if (user.getRole().getName().name().equals("USER")) {
            throw new ForbiddenAccessException("Can only access if you are an Admin");
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            User user1 = new User(userId);
            Page<Account> accounts = accountRepository.findAccountsByUserId(user1, pageable);
            List<AccountDto> accountDtoList = accounts.stream().map(accountMapper::convertToDto).collect(Collectors.toList());
            final String GET_ACCOUNTS_URL = request.getRequestURL().toString() + "?page=";
            final String previousPageURL = GET_ACCOUNTS_URL + accounts.previousOrFirstPageable().getPageNumber();
            final String nextPageURL = GET_ACCOUNTS_URL + accounts.nextOrLastPageable().getPageNumber();
            PaginatedAccountsDto paginatedAccountsDto = new PaginatedAccountsDto(
                    accountDtoList,
                    previousPageURL,
                    nextPageURL);
            return paginatedAccountsDto;
        }
    }

    private double getTransactionLimitForCurrency(Currency currency) {
        return switch (currency) {
            case ARS -> 300000;
            case USD -> 1000;
        };
    }
}
