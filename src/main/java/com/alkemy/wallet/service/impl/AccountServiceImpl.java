package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.entity.User;
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
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.USD;

@Service
@RequiredArgsConstructor(onConstructor_ ={@Lazy})
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
    public List<AccountBalanceResponseDto> getAccountBalance(String token) {
        double valorPesoArs = 160.41;
        double valorPesoUsd = 0.0062;
        double porcentaje = 10;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        long idUser = authService.getUserFromToken(token).getId();
        List<Account> accounts = repository.findAccountByUserId(idUser);
        if (accounts.isEmpty())
            throw new IllegalArgumentException("User not available");

        List<AccountBalanceResponseDto> accountBalanceList = new ArrayList<>();

        for (Account account : accounts) {
            AccountBalanceResponseDto accountBalanceResponseDTO;

            LocalDate dateDB = LocalDate.of
                    (account.getCreationDate().getYear(),
                            account.getCreationDate().getMonth(),
                            account.getCreationDate().getDayOfWeek().getValue());

            Period duration = Period.between(dateDB, LocalDate.now());
            accountBalanceResponseDTO = new AccountBalanceResponseDto();
            if (duration.getMonths() > 0) {
                double aumento = (account.getBalance() * porcentaje) / 100;
                accountBalanceResponseDTO.setFixedTermDeposit(account.getBalance() + (aumento * duration.getMonths()));
            }
            if (account.getCurrency().equals(ARS)) {
                double arsToUsd = Double.parseDouble(decimalFormat.format((account.getBalance() * valorPesoArs)));
                accountBalanceResponseDTO.setBalanceUsd(arsToUsd);
                accountBalanceResponseDTO.setBalanceArs(account.getBalance());
                accountBalanceList.add(accountBalanceResponseDTO);
            } else if (account.getCurrency().equals(USD)) {
                double usdToArs = Double.parseDouble(decimalFormat.format((account.getBalance() * valorPesoUsd)));
                accountBalanceResponseDTO.setBalanceUsd(account.getBalance());
                accountBalanceResponseDTO.setBalanceArs(usdToArs);
                accountBalanceList.add(accountBalanceResponseDTO);
            }
        }
        return accountBalanceList;
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        List<Account> accounts = repository.findAccountByUserId(userId);
        if (accounts.isEmpty())
            throw new NoSuchElementException("The user does not have accounts yet");
        return accounts;
    }

    @Override
    public AccountResponseDto updateAccount(Long id, Double newTransactionLimit, String token) {
        User user = authService.getUserFromToken(token);
        Account accountToUpdate = null;
        for (Account account : user.getAccounts()) {
            if (account.getId().equals(id)) {
                accountToUpdate = account;
            }
        }
        if (accountToUpdate == null) {
            throw new EntityNotFoundException(String.format("The Account with id:%s does not exist or does not belong to the user", id));
        }
        accountToUpdate.setTransactionLimit(newTransactionLimit);

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
