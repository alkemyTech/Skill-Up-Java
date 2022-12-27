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
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.*;

import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.USD;
import static com.alkemy.wallet.model.constant.TransactionTypeEnum.INCOME;
import static com.alkemy.wallet.model.constant.TransactionTypeEnum.PAYMENT;
import static com.alkemy.wallet.utils.PageUtil.PAGE_SIZE;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class AccountServiceImpl implements IAccountService {

    protected static final double TRANSACTION_LIMIT_USD = 1000.0;
    protected static final double TRANSACTION_LIMIT_ARS = 300000.0;

    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final IAuthService authService;
    private final IUserService userService;
    private final CustomMessageSource messageSource;

    @Override
    public AccountResponseDto createNewAccount(AccountRequestDto accountRequestDto) {
        User user = userService.getUserByEmail(authService.getEmailFromContext());
        user.getAccounts().forEach(account -> {
            if (accountRequestDto.getCurrency().equalsIgnoreCase(account.getCurrency().name()))
                throw new EntityExistsException(messageSource
                        .message("account.duplicated",
                                new String[]{accountRequestDto.getCurrency().toUpperCase()}));
        });

        AccountCurrencyEnum currency = getCurrencyType(accountRequestDto.getCurrency());
        Account account;
        if (currency.equals(ARS))
            account = buildAccount(user, currency, TRANSACTION_LIMIT_ARS);
        else
            account = buildAccount(user, currency, TRANSACTION_LIMIT_USD);

        userService.addAccountToUser(user, account);
        return accountMapper.entity2Dto(accountRepository.save(account));
    }

    @Override
    public List<Account> createDefaultAccounts(User user) {
        Account arsAccount = buildAccount(user, ARS, TRANSACTION_LIMIT_ARS);
        Account usdAccount = buildAccount(user, USD, TRANSACTION_LIMIT_USD);

        accountRepository.save(arsAccount);
        accountRepository.save(usdAccount);

        List<Account> accountList = new ArrayList<>();
        accountList.add(arsAccount);
        accountList.add(usdAccount);

        return accountList;
    }

    @Override
    public AccountResponseDto updateAccount(Long id, UpdateAccountRequestDto updateAccountRequestDto) {
        Account account = getAccountById(id);
        if (!account.getUser().getEmail().equals(authService.getEmailFromContext()))
            throw new IllegalArgumentException(
                    messageSource.message("entity.out-of-bound", new String[]{"Account", "id:", id.toString()}));
        account.setTransactionLimit(updateAccountRequestDto.getTransactionLimit());
        return accountMapper.entity2Dto(account);
    }

    @Override
    public void editBalanceAndSave(Account account, Double newBalance) {
        account.setBalance(newBalance);
    }

    @Override
    public AccountBalanceResponseDto getAccountBalance() {
        User user = userService.getUserByEmail(authService.getEmailFromContext());

        double incomesUSD = 0.0;
        double paymentsUSD = 0.0;

        double incomesARS = 0.0;
        double paymentsARS = 0.0;

        for (Transaction transaction : user.getTransactions()) {
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

        for (FixedTermDeposit fixedTermDeposit : user.getFixedTermDeposits()) {
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
    public Account getAccountByCurrencyAndUserId(String currency, Long userId) {
        Optional<Account> account = accountRepository.findByCurrencyAndUserId(currency, userId);
        return account.orElseThrow(() ->
                new NullPointerException(
                        messageSource.message("entity.not-found",
                                new String[]{"Account", "currency ", currency})));
    }

    @Override
    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElseThrow(() ->
                new NullPointerException(
                        messageSource.message("entity.not-found",
                                new String[]{"Account", "id", id.toString()})));
    }

    @Override
    public List<AccountResponseDto> getAccountListByUserId(Long userId) {
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        if (accounts.isEmpty())
            throw new NoSuchElementException(messageSource.message("account.empty-list", null));
        return accountMapper.entityList2DtoList(accounts);
    }

    @Override
    public Page<AccountResponseDto> getAllAccounts(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        pageable.next().getPageNumber();
        return accountRepository.findAll(pageable).map(accountMapper::entity2Dto);
    }

    protected AccountCurrencyEnum getCurrencyType(String type) {
        if (USD.name().equalsIgnoreCase(type))
            return USD;
        if (ARS.name().equalsIgnoreCase(type))
            return ARS;
        throw new InputMismatchException(messageSource.message("account.invalid-currency", null));
    }

    protected Account buildAccount(User user, AccountCurrencyEnum currency, Double transactionLimit) {
        Account account = new Account();
        account.setCurrency(currency);
        account.setTransactionLimit(transactionLimit);
        account.setUser(user);
        return account;
    }
}
