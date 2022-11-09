package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.controller.exception.Mistake;
import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.*;
import com.alkemy.wallet.model.mapper.AccountMapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;
    private final IUserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseDto save(AccountRequestDto request) {
        return null;
    }

    @Override
    public List<AccountBalanceDto> getAccountBalance(long idUser, int na) {
        List<Account> account = accountRepository.findAccountByUserId(idUser);
        if (account.isEmpty())
            throw new Mistake("Usuario no disponible");

        List<AccountBalanceDto> accountBalanceList = new ArrayList<>();

        for (Account value : account) {
            AccountBalanceDto accountBalanceDTO = null;

            LocalDate dateDB = LocalDate.of
                    (value.getCreationDate().getYear(),
                            value.getCreationDate().getMonth(),
                            value.getCreationDate().getDayOfWeek().getValue());

            Period duration = Period.between(dateDB, LocalDate.now());
            accountBalanceDTO = new AccountBalanceDto();
            if (duration.getMonths() > 0) {
                accountBalanceDTO.setFixedTermDeposit(value.getBalance() * (282 * duration.getMonths()));
            }
            if (value.getCurrency().equals(AccountCurrencyEnum.ARS)) {
                accountBalanceDTO.setBalanceUSD(value.getBalance() / 282);
                accountBalanceDTO.setBalanceARS(value.getBalance());
                accountBalanceList.add(accountBalanceDTO);
            }

            if (value.getCurrency().equals(AccountCurrencyEnum.USD)) {
                accountBalanceDTO.setBalanceUSD(value.getBalance() * 282);
                accountBalanceDTO.setBalanceARS(value.getBalance());
                accountBalanceList.add(accountBalanceDTO);
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
    public String sendMoney(long idUser, long idTargetUser, double amount, String money, int typeMoney, String type) {
        String noDisponible = " no esta disponible";
        if (idTargetUser == idUser)
            throw new Mistake("Error no se puede enviar dinero al mismo usuario");

        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty())
            throw new Mistake("El usuario con id " + idUser + noDisponible);

        Optional<User> targetUser = userRepository.findById(idTargetUser);
        if (targetUser.isEmpty())
            throw new Mistake("El usuario con id " + idTargetUser + noDisponible);

        Optional<Account> accountUser = accountRepository.findTopByUserId(idUser);
        if (accountUser.isEmpty())
            throw new Mistake("La account con id " + idUser + noDisponible);

        Optional<Account> accountTargetUser = accountRepository.findTopByUserId(idTargetUser);
        if (accountTargetUser.isEmpty())
            throw new Mistake("La account con id " + idTargetUser + noDisponible);

        specificTypeOfTransaction(type);
        specificTypeOfMoney(typeMoney, money, accountUser.get(), accountTargetUser.get());

        if (accountUser.get().getBalance() < amount)
            throw new Mistake("Error valor disponible superado");

        if (amount > accountUser.get().getTransactionLimit())
            throw new Mistake("Error supera el limite de transacciones");

        double balanceUser = accountUser.get().getBalance() - amount;
        double targetUserBalance = accountTargetUser.get().getBalance() + amount;

        accountUser.get().setBalance((balanceUser));
        accountTargetUser.get().setBalance(targetUserBalance);

        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setDescription("Transacción exitosa");
        transaction.setType(specificTypeOfTransaction(type));
        transaction.setUser(targetUser.get());
        transaction.setAccount(accountUser.get());

        accountRepository.save(accountTargetUser.get());
        accountRepository.save(accountUser.get());
        transactionRepository.save(transaction);

        return "Operación realizada exitosamente";
    }


    public void specificTypeOfMoney(int typeMoney, String money, Account accountUser, Account accountTargetUser) {
        String error = "Error solo puede enviar dinero en ";
        if (typeMoney == 1 && (!accountUser.getCurrency().equals(AccountCurrencyEnum.ARS) || !accountTargetUser.getCurrency().equals(AccountCurrencyEnum.ARS)))
            throw new Mistake(error + money);
        else {
            if (typeMoney == 2 && (!accountUser.getCurrency().equals(AccountCurrencyEnum.USD) || !accountTargetUser.getCurrency().equals(AccountCurrencyEnum.USD)))
                throw new Mistake(error + money);
        }
    }

    public TransactionTypeEnum specificTypeOfTransaction(String type) {
        if (TransactionTypeEnum.PAYMENT.name().equalsIgnoreCase(type))
            return TransactionTypeEnum.PAYMENT;
        else if (TransactionTypeEnum.INCOME.name().equalsIgnoreCase(type))
            return TransactionTypeEnum.INCOME;
        else if (TransactionTypeEnum.DEPOSIT.name().equalsIgnoreCase(type))
            return TransactionTypeEnum.DEPOSIT;
        else
            throw new Mistake("El tipo ingresado es incorrecto");
    }
}
