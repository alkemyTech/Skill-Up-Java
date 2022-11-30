package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.constant.AccountCurrencyEnum;
import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateAccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.AccountMapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthenticationService;
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
import java.util.*;

import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.USD;
import static com.alkemy.wallet.model.constant.FinalValue.PAGE_SIZE;
import static com.alkemy.wallet.model.constant.TransactionTypeEnum.INCOME;
import static com.alkemy.wallet.model.constant.TransactionTypeEnum.PAYMENT;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class AccountServiceImpl implements IAccountService {

    protected static final double TRANSACTION_LIMIT_USD = 1000.0;
    protected static final double TRANSACTION_LIMIT_ARS = 300000.0;

    private final IAccountRepository repository;
    private final AccountMapper mapper;
    private final IAuthenticationService authService;
    private final IUserService userService;

    @Override
    public Account getById(Long id) {
        Optional<Account> account = repository.findById(id);
        return account.orElseThrow(() ->
                new NullPointerException(String.format("Account with id %s was not found", id)));
    }

    @Override
    public Account getByCurrencyAndUserId(String currency, Long userId) {
        Optional<Account> account = repository.findByCurrencyAndUserId(currency, userId);
        return account.orElseThrow(() ->
                new NullPointerException("The account doesn't exist or the user is not present"));
    }

    @Override
    public void editBalanceAndSave(Account account, Double newBalance) {
        account.setBalance(newBalance);
        repository.save(account);
    }

    @Override
    public AccountResponseDto create(AccountRequestDto request) {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());
        loggedUser.getAccounts().forEach(account -> {
            if (request.getCurrency().equalsIgnoreCase(account.getCurrency().name()))
                throw new EntityExistsException(String
                        .format("An account in %s already exist", request.getCurrency().toUpperCase()));
        });

        AccountCurrencyEnum currency = getTypeOfCurrency(request.getCurrency());
        Account account;
        if (currency.equals(ARS))
            account = createNewAccount(loggedUser, currency, TRANSACTION_LIMIT_ARS);
        else
            account = createNewAccount(loggedUser, currency, TRANSACTION_LIMIT_USD);

        userService.addAccount(loggedUser, account);
        return mapper.entity2Dto(repository.save(account));
    }

    @Override
    public List<Account> createDefaultAccounts(User user) {
        Account arsAccount = createNewAccount(user, ARS, TRANSACTION_LIMIT_ARS);
        Account usdAccount = createNewAccount(user, USD, TRANSACTION_LIMIT_USD);

        repository.save(arsAccount);
        repository.save(usdAccount);

        List<Account> accountList = new ArrayList<>();
        accountList.add(arsAccount);
        accountList.add(usdAccount);

        return accountList;
    }

    @Override
    public AccountBalanceResponseDto getBalance() {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());

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
                amountFixedDepositsARS = amountFixedDepositsARS
                        + fixedTermDeposit.getAmount() + fixedTermDeposit.getInterest();

            if (fixedTermDeposit.getAccount().getCurrency().equals(USD))
                amountFixedDepositsUSD = amountFixedDepositsUSD
                        + fixedTermDeposit.getAmount() + fixedTermDeposit.getInterest();
        }

        return AccountBalanceResponseDto.builder()
                .balanceUSD(generalBalanceUSD)
                .balanceARS(generalBalanceARS)
                .fixedTermDepositUSD(Math.round(amountFixedDepositsUSD * 100d) / 100d)
                .fixedTermDepositARS(Math.round(amountFixedDepositsARS * 100d) / 100d)
                .build();
    }

    @Override
    public List<AccountResponseDto> getListByUserId(Long userId) {
        List<Account> accounts = repository.findAccountsByUserId(userId);
        if (accounts.isEmpty())
            throw new NoSuchElementException("The user does not have accounts yet or the user does not exist");
        return mapper.entityList2DtoList(accounts);
    }

    @Override
    public AccountResponseDto update(Long id, UpdateAccountRequestDto request) {
        Account account = getById(id);
        if (!account.getUser().getEmail().equals(authService.getEmailFromContext()))
            throw new InputMismatchException("The account does not exist or does not belong to current user");
        account.setTransactionLimit(request.getTransactionLimit());
        return mapper.entity2Dto(repository.save(account));
    }

    @Override
    public Page<AccountResponseDto> getAll(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        pageable.next().getPageNumber();
        return repository.findAll(pageable).map(mapper::entity2Dto);
    }

    protected AccountCurrencyEnum getTypeOfCurrency(String type) {
        if (USD.name().equalsIgnoreCase(type))
            return USD;
        if (ARS.name().equalsIgnoreCase(type))
            return ARS;
        throw new IllegalArgumentException("Account currency can only be ARS or USD");
    }

    protected Account createNewAccount(User user, AccountCurrencyEnum currency, Double transactionLimit) {
        return Account.builder()
                .creationDate(LocalDateTime.now())
                .balance(0.0)
                .currency(currency)
                .softDelete(false)
                .transactionLimit(transactionLimit)
                .user(user)
                .build();
    }
}
