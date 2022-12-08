package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.BasicAccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.*;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class AccountService implements IAccountService {

    private IAccountRepository accountRepository;

    private IUserService userService;

    private ModelMapper mapper;


    @Autowired
    private JwtUtil jwtUtil;

    public AccountService(IAccountRepository accountRepository, IUserService userService, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public BasicAccountDto createAccount(Account account) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = mapper.map(userService.findByEmail(email), User.class);
        if (user != null) {
            List<Account> accounts = accountRepository.findAllByUser_Email(email);

            if (accounts.stream()
                    .anyMatch(c -> c.getCurrency().equals(account.getCurrency()))) {
                throw new ResourceFoundException("Account with " + account.getCurrency() + " is already created");
            }
        }

        account.setUser(user);
        account.setCreationDate(new Date());

        return mapper.map(accountRepository.save(account), BasicAccountDto.class);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByUserId(Long userId) throws EmptyResultDataAccessException {
        List<Account> accounts = accountRepository.findAllByUser_Id(userId);

        if (accounts.isEmpty()) {
            throw new EmptyResultDataAccessException("User has no accounts", 1);
        }
        return accounts;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountDto> findAllAccountsPageable(int page) throws EmptyResultDataAccessException {

        Pageable pageable = PageRequest.of(page, 10);

        Page<AccountDto> pageAccounts = accountRepository.findAll(pageable).map((account) ->
                mapper.map(account, AccountDto.class));

        return pageAccounts;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasicAccountDto> getAccountsByUserEmail(String email) throws EmptyResultDataAccessException {
        List<Account> accounts = accountRepository.findAllByUser_Email(email);

        if (accounts.isEmpty()) {
            throw new EmptyResultDataAccessException("User has no accounts", 1);
        }
        return accounts.stream().map(account ->
                mapper.map(account, BasicAccountDto.class)
        ).toList();
    }

    @Override
    public Account getAccountByCurrency(Long userId, Currency currency) {

        List<Account> userAccounts = getAccountsByUserId(userId);
        for (Account account : userAccounts) {
            if (account.getCurrency() == currency) {
                return account;
            }
        }
        return null;
    }

    @Override
    public boolean checkAccountLimit(Account senderAccount, TransactionDto transactionDto) {
        if (transactionDto.getAmount() < senderAccount.getTransactionLimit())
            return true;
        else throw new AccountLimitException("Account transaction limit exceeded");
    }

    public ResponseEntity<?> updateAccount(Long id, AccountUpdateDto newTransactionLimit, String token) {
        try {
            userService.checkLoggedUser(token);
            Account account = accountRepository.findById(id).orElseThrow();
            mapper.map(newTransactionLimit, account);
            Account accountUpdated = accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.map(accountUpdated, BasicAccountDto.class));
        } catch (UserNotLoggedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
    }

    @Override
    public boolean checkAccountExistence(Long user_id, Currency currency) {
        List<Account> accounts = accountRepository.findAllByUser_Id(user_id);
        for (Account account : accounts) {
            if (account.getCurrency() == currency) {
                throw new AccountAlreadyExistsException("Account already exists");
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<?> postAccount(BasicAccountDto basicAccountDto, String token) {
        try {
            userService.checkLoggedUser(token);
//            UserDto loggedUser = userService.findByEmail(jwtUtil.getValue(token));
//            checkAccountExistence(loggedUser.getId(), basicAccountDto.getCurrency());
            return ResponseEntity.status(HttpStatus.OK).body(createAccount(mapper.map(basicAccountDto, Account.class)));

        } catch (UserNotLoggedException | AccountAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
        }
    }
}
