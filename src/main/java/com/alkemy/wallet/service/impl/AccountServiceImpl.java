package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateAccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.*;
import com.alkemy.wallet.model.mapper.AccountMapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.USD;
import static com.alkemy.wallet.model.entity.TransactionTypeEnum.INCOME;
import static com.alkemy.wallet.model.entity.TransactionTypeEnum.PAYMENT;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository repository;
    private final AccountMapper mapper;
    private final IAuthService authService;
    private final IUserService userService;

    @Override
    public Account getAccountById(Long id) {
        Optional<Account> account = repository.findById(id);
        if (account.isEmpty())
            throw new NoSuchElementException(String.format("Account with id %s was not found", id));
        return account.get();
    }

    @Override
    public Account getByCurrencyAndUserId(String currency, Long userId) {
        Optional<Account> response = repository.findByCurrencyAndUserId(currency, userId);
        if (response.isEmpty())
            throw new NoSuchElementException("The account doesn't exist or the user is not present");
        return response.get();
    }

    @Override
    public void editBalanceAndSave(Account account, Double newBalance) {
        account.setBalance(newBalance);
        repository.save(account);
    }

    @Override
    public AccountResponseDto createAccount(AccountRequestDto request, String token) {
        User loggedUser = authService.getUserFromToken(token);
        loggedUser.getAccounts().forEach(account -> {
            if (request.getCurrency().equalsIgnoreCase(account.getCurrency().name()))
                throw new EntityExistsException(String.format("An account in %s already exist", request.getCurrency().toUpperCase()));
        });
        AccountCurrencyEnum currency;
        double transactionLimit;
        if (specificTypeOfCurrency(request.getCurrency()).equals(ARS)) {
            currency = ARS;
            transactionLimit = 300000.0;
        } else {
            currency = USD;
            transactionLimit = 1000.0;
        }
        Account account = mapper.dto2Entity(request, currency, transactionLimit, loggedUser);
        userService.addAccount(loggedUser, account);
        return mapper.entity2Dto(repository.save(account));
    }

    @Override
    public List<Account> createUserAccounts(User user) {

        Account usdAccount = new Account();
        usdAccount.setCreationDate(LocalDateTime.now());
        usdAccount.setBalance(0.0);
        usdAccount.setCurrency(USD);
        usdAccount.setSoftDelete(false);
        usdAccount.setTransactionLimit(1000.0);
        usdAccount.setUser(user);

        Account arsAccount = new Account();
        arsAccount.setCreationDate(LocalDateTime.now());
        arsAccount.setBalance(0.0);
        arsAccount.setCurrency(ARS);
        arsAccount.setSoftDelete(false);
        arsAccount.setTransactionLimit(300000.0);
        arsAccount.setUser(user);

        repository.save(usdAccount);
        repository.save(arsAccount);

        List<Account> accountList = new ArrayList<>();
        accountList.add(usdAccount);
        accountList.add(arsAccount);

        return accountList;
    }

    @Override
    public AccountBalanceResponseDto getAccountBalance(String token) {
        User loggedUser = authService.getUserFromToken(token);

        double incomesUSD = 0.0;
        double paymentsUSD = 0.0;

        double incomesARS = 0.0;
        double paymentsARS = 0.0;

        for (Transaction transaction : loggedUser.getTransactions()) {
            if (transaction.getType().equals(INCOME) && transaction.getAccount().getCurrency().equals(ARS))
                incomesARS = incomesARS + transaction.getAmount();
            if (transaction.getType().equals(INCOME) && transaction.getAccount().getCurrency().equals(USD))
                incomesUSD = incomesUSD + transaction.getAmount();

            if (transaction.getType().equals(PAYMENT) && transaction.getAccount().getCurrency().equals(ARS))
                paymentsARS = paymentsARS + transaction.getAmount();
            if (transaction.getType().equals(PAYMENT) && transaction.getAccount().getCurrency().equals(USD))
                paymentsUSD = paymentsUSD + transaction.getAmount();
        }
        double generalBalanceARS = incomesARS - paymentsARS;
        double generalBalanceUSD = incomesUSD - paymentsUSD;

        double amountFixedDepositsARS = 0.0;
        double amountFixedDepositsUSD = 0.0;

        for (FixedTermDeposit fixedTermDeposit : loggedUser.getFixedTermDeposits()) {
            if (fixedTermDeposit.getAccount().getCurrency().equals(ARS))
                amountFixedDepositsARS = amountFixedDepositsARS + fixedTermDeposit.getAmount();

            if (fixedTermDeposit.getAccount().getCurrency().equals(USD))
                amountFixedDepositsUSD = amountFixedDepositsUSD + fixedTermDeposit.getAmount();
        }

        return AccountBalanceResponseDto.builder()
                .balanceUSD(generalBalanceUSD)
                .balanceARS(generalBalanceARS)
                .fixedTermDepositUSD(amountFixedDepositsUSD)
                .fixedTermDepositARS(amountFixedDepositsARS)
                .build();
    }

    @Override
    public List<AccountResponseDto> getAccountsByUserId(Long userId) {
        List<Account> accounts = repository.findAccountsByUserId(userId);
        if (accounts.isEmpty())
            throw new NoSuchElementException("The user does not have accounts yet");
        return mapper.entityList2DtoList(accounts);
    }

    @Override
    public AccountResponseDto updateAccount(Long id, UpdateAccountRequestDto request, String token) {
        User user = authService.getUserFromToken(token);
        Account accountToUpdate = getAccountById(id);
            if (!user.getAccounts().contains(accountToUpdate))
                throw new AccessDeniedException("The account does not exist or does not belong to current user");
        accountToUpdate.setTransactionLimit(request.getTransactionLimit());
        return mapper.entity2Dto(repository.save(accountToUpdate));
    }

    @Override
    public Page<AccountResponseDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        pageable.next().getPageNumber();
        return repository.findAll(pageable).map(mapper::entity2Dto);
    }

    private AccountCurrencyEnum specificTypeOfCurrency(String type) {
        if (USD.name().equalsIgnoreCase(type))
            return USD;
        return ARS;
    }
}
