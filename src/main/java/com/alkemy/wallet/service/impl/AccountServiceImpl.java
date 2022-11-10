package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.controller.exception.Mistake;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.*;
import com.alkemy.wallet.model.mapper.AccountMapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    private final ITransactionRepository transactionRepository;
    private final IUserService userService;
    private final AccountMapper accountMapper;
    private final IAuthService authService;

    @Override
    public Account getAccountById(long idAccount) {
       return accountRepository.findById(idAccount).get();
    }

    //ToDo: Guardar una cuenta nueva
    @Override
    public AccountResponseDto save(Account entity) {
        return null;
    }

    //ToDo: Implementar patron de creacion
    @Override
    public List<AccountResponseDto> createUserAccounts(Long userId) {
        User accountUser = userService.findById((userId)).get();
        if (accountUser == null) {
            throw new EntityNotFoundException(String.format("The user with userid: %s does not exist", userId));
        } else {
            //creating accounts
            AccountCurrencyEnum currencyUSD = USD;
            AccountCurrencyEnum currencyARS = ARS;

            Account USDAccount = new Account();
            USDAccount.setUser(accountUser);
            USDAccount.setCreationDate(LocalDateTime.now());
            USDAccount.setBalance(0.0);
            USDAccount.setCurrency(currencyUSD);
            USDAccount.setSoftDelete(false);
            USDAccount.setTransactionLimit(1000.0);

            Account ARSAccount = new Account();
            ARSAccount.setUser(accountUser);
            ARSAccount.setCreationDate(LocalDateTime.now());
            ARSAccount.setBalance(0.0);
            ARSAccount.setCurrency(currencyARS);
            ARSAccount.setSoftDelete(false);
            ARSAccount.setTransactionLimit(300000.0);

            accountRepository.save(USDAccount);
            accountRepository.save(ARSAccount);

            userService.save(accountUser);

            List<Account> accountList= new ArrayList<Account>();
            accountList.add(USDAccount);
            accountList.add(ARSAccount);

            return accountMapper.entityList2DtoList(accountList);
        }
    }

    @Override
    public  AccountResponseDto editAccountBalance(long idAccount, Double newBalance){
        Account account = accountRepository.findById(idAccount).get();
        if(account !=null) {
            if (account.getBalance() >= newBalance) {
                account.setBalance(newBalance);
                return accountMapper.entity2Dto(accountRepository.save(account));
            }
        }else{
            throw new EntityNotFoundException(String.format("Account with id: %s was not found", idAccount));
            }
        return null;
    }

    @Override
    public List<AccountBalanceResponseDto> getAccountBalance(String token) {
        long idUser = authService.getUserFromToken(token).getId();
        List<Account> account = accountRepository.findAccountByUserId(idUser);
        if (account.isEmpty())
            throw new Mistake("Usuario no disponible");

        List<AccountBalanceResponseDto> accountBalanceList = new ArrayList<>();

        for (Account value : account) {
            AccountBalanceResponseDto accountBalanceResponseDTO;

            LocalDate dateDB = LocalDate.of
                    (value.getCreationDate().getYear(),
                            value.getCreationDate().getMonth(),
                            value.getCreationDate().getDayOfWeek().getValue());

            Period duration = Period.between(dateDB, LocalDate.now());
            accountBalanceResponseDTO = new AccountBalanceResponseDto();
            if (duration.getMonths() > 0) {
                accountBalanceResponseDTO.setFixedTermDeposit(value.getBalance() * (282 * duration.getMonths()));
            }
            if (value.getCurrency().equals(ARS)) {
                accountBalanceResponseDTO.setBalanceUsd(value.getBalance() / 282);
                accountBalanceResponseDTO.setBalanceArs(value.getBalance());
                accountBalanceList.add(accountBalanceResponseDTO);
            }

            if (value.getCurrency().equals(USD)) {
                accountBalanceResponseDTO.setBalanceUsd(value.getBalance() * 282);
                accountBalanceResponseDTO.setBalanceArs(value.getBalance());
                accountBalanceList.add(accountBalanceResponseDTO);
            }
        }
        return accountBalanceList;
    }

    //ToDo Solo administrador
    @Override
    public List<AccountResponseDto> getAccountUserById(long idUser) {
        List<Account> account = accountRepository.findAccountByUserId(idUser);
        if (account.isEmpty())
            return Collections.emptyList();
        return accountMapper.entityList2DtoList(account);
    }

    @Override
    public String sendMoney(long idTargetUser, double amount, String money, int typeMoney, String type, String token) {
        String noDisponible = " no esta disponible";
        long idUser = authService.getUserFromToken(token).getId();
        if (idTargetUser == idUser)
            throw new Mistake("Error no se puede enviar dinero al mismo usuario");

        Optional<User> user = userService.findById(idUser);
        if (user.isEmpty())
            throw new Mistake("El usuario con id " + idUser + noDisponible);

        Optional<User> targetUser = userService.findById(idTargetUser);
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
        //ToDo: Cambiar repository por transactionService
        transactionRepository.save(transaction);

        return "Operación realizada exitosamente";
    }


    public void specificTypeOfMoney(int typeMoney, String money, Account accountUser, Account accountTargetUser) {
        String error = "Error solo puede enviar dinero en ";
        if (typeMoney == 1 && (!accountUser.getCurrency().equals(ARS) || !accountTargetUser.getCurrency().equals(ARS)))
            throw new Mistake(error + money);
        else {
            if (typeMoney == 2 && (!accountUser.getCurrency().equals(USD) || !accountTargetUser.getCurrency().equals(USD)))
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
