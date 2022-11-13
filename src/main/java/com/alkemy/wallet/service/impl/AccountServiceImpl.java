package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.controller.exception.ExceptionCustom;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.AccountMapper;
import com.alkemy.wallet.model.response.AccountResponseDto;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.USD;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final IAuthService authService;

    private static final String EL_USUARIO_CON_ID = "El usuario con id ";

    @Override
    public Account getAccountById(long idAccount) {
        Optional<Account> account = accountRepository.findById(idAccount);
        if (account.isEmpty())
            throw new ExceptionCustom(EL_USUARIO_CON_ID + idAccount + " no esta disponible");
        return account.get();
    }

    //Se debe hacer el PR de esta funcionalidad
    @Override
    public AccountResponseDto createAccount(AccountResponseDto dto) {
        return null;
    }

    @Override
    public List<Account> createUserAccounts() {

        Account usdAccount = new Account();
        usdAccount.setCreationDate(LocalDateTime.now());
        usdAccount.setBalance(0.0);
        usdAccount.setCurrency(USD);
        usdAccount.setSoftDelete(false);
        usdAccount.setTransactionLimit(1000.0);

        Account arsAccount = new Account();
        arsAccount.setCreationDate(LocalDateTime.now());
        arsAccount.setBalance(0.0);
        arsAccount.setCurrency(ARS);
        arsAccount.setSoftDelete(false);
        arsAccount.setTransactionLimit(300000.0);

        accountRepository.save(usdAccount);
        accountRepository.save(arsAccount);

        List<Account> accountList = new ArrayList<>();
        accountList.add(usdAccount);
        accountList.add(arsAccount);

        return accountList;
    }

    @Override
    public AccountResponseDto editAccountBalance(long idAccount, Double newBalance) {
        Optional<Account> account = accountRepository.findById(idAccount);
        if (account.isEmpty())
            throw new EntityNotFoundException(String.format("Account with id: %s was not found", idAccount));

        //TODO cambiar mensaje
        if (account.get().getBalance() <= newBalance)
            throw new EntityNotFoundException(String.format("Account with id: %s was not found", idAccount));

        account.get().setBalance(newBalance);
        return accountMapper.entity2Dto(accountRepository.save(account.get()));
    }

    @Override
    public List<AccountBalanceResponseDto> getAccountBalance(String token) {
        double valorPesoArs = 160.41;
        double valorPesoUsd = 0.0062;
        double porcentaje = 10;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        long idUser = authService.getUserFromToken(token).getId();
        List<Account> accounts = accountRepository.findAccountByUserId(idUser);
        if (accounts.isEmpty())
            throw new ExceptionCustom("Usuario no disponible");

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
    public List<AccountResponseDto> getAccountUserById(long idUser) {
        List<Account> account = accountRepository.findAccountByUserId(idUser);
        if (account.isEmpty())
            return Collections.emptyList();
        return accountMapper.entityList2DtoList(account);
    }

    @Override
    public AccountResponseDto updateAccount(long accountId, double newTransactionLimit, String token) {
        User user = authService.getUserFromToken(token);
        Account accountToUpdate = null;
        for (Account account : user.getAccounts()) {
            if (account.getId().equals(accountId)) {
                accountToUpdate = account;
            }
        }
        if (accountToUpdate == null) {
            throw new EntityNotFoundException(String.format("The Account with id:%s does not exist or does not belong to the user", accountId));
        }
        accountToUpdate.setTransactionLimit(newTransactionLimit);

        return accountMapper.entity2Dto(accountRepository.save(accountToUpdate));
    }

    @Override
    public Optional<Account> findTopByUserId(Long userId) {
        Optional<Account> account = accountRepository.findTopByUserId(userId);
        if (account.isEmpty())
            throw new ExceptionCustom("La cuenta con el id " + userId + " no esta disponible");
        return account;
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
