package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.AccountAlreadyExistsException;
import com.alkemy.wallet.exception.AccountLimitException;
import com.alkemy.wallet.exception.ResourceFoundException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class AccountService implements IAccountService {

    private static final Double LIMIT_USD = 1000D;

    private static final Double LIMIT_ARS = 300000D;


    private IAccountRepository accountRepository;

    private IUserService userService;

    private ModelMapper mapper;

    public AccountService(IAccountRepository accountRepository, IUserService userService, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = mapper.map(userService.findByEmail(email), User.class);
        if (user != null) {
            List<Account> accounts = accountRepository.findAllByUser_Email(email);

            if (accounts.stream()
                    .anyMatch(c -> c.getCurrency().equals(accountDto.getCurrency()))) {
                throw new ResourceFoundException("Account with " + accountDto.getCurrency() + " is already created");
            }
        }

//        Account account = mapper.map(accountDto, Account.class);
//
//        account.setUser(user);

        switch (accountDto.getCurrency()) {
            case usd:
                accountDto.setTransactionLimit(LIMIT_USD);
                break;
            case ars:
                accountDto.setTransactionLimit(LIMIT_ARS);
                break;

        }

        Account account = mapper.map(accountDto, Account.class);
        account.setUser(user);
        account.setBalance(1000D);
        account.setCreationDate(new Date());

        return mapper.map(accountRepository.save(account), AccountDto.class);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByUserId(Long userId) throws EmptyResultDataAccessException {
        List<Account> accounts = accountRepository.getAccountsByUser(userId);

        if (accounts.isEmpty()) {
            throw new EmptyResultDataAccessException("El usuario no posee cuentas", 1);
        }
        return accounts.stream().map(account ->
                mapper.map(account, AccountDto.class)
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByUserEmail(String email) throws EmptyResultDataAccessException {
        List<Account> accounts = accountRepository.findAllByUser_Email(email);

        if (accounts.isEmpty()) {
            throw new EmptyResultDataAccessException("El usuario no posee cuentas", 1);
        }
        return accounts.stream().map(account ->
                mapper.map(account, AccountDto.class)
        ).toList();
    }

    @Override
    public AccountDto getAccountByCurrency(Long userId, Currency currency) {

        List<AccountDto> userAccounts = getAccountsByUserId(userId);
        for (AccountDto accountDto : userAccounts) {
            if (accountDto.getCurrency() == currency) {
                return accountDto;
            }
        }
        return null;
    }

    @Override

    public boolean checkAccountLimit(AccountDto senderAccount, TransactionDto destinedTransactionDto) {
        if (destinedTransactionDto.getAmount() <
                senderAccount.getBalance() && destinedTransactionDto.getAmount() < senderAccount.getTransactionLimit())
            return true;
        else throw new AccountLimitException("No Tiene dinero suficiente o excedio el limite de transferencia");
    }

    public AccountDto updateAccount(Long id, AccountUpdateDto newTransactionLimit) {
        Account account = accountRepository.findById(id).orElseThrow();
        mapper.map(newTransactionLimit, account);
        Account accountUpdated = accountRepository.save(account);
        return mapper.map(accountUpdated, AccountDto.class);
    }

    @Override
    public boolean checkAccountExistence(Long user_id, Currency currency) {
        List<Account> accounts = accountRepository.getAccountsByUser(user_id);
        for (Account account : accounts) {
            if (account.getCurrency() == currency) {
                throw new AccountAlreadyExistsException("La cuenta que quiere crear ya existe");
            }
        }
        return false;
    }
}
