package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.controller.exception.ExceptionCustom;
import com.alkemy.wallet.model.entity.*;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.USD;
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository repository;
    private final IUserService userService;
    private final IAuthService authService;
    private final IAccountService accountService;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteTransaction(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Map<String,String> sendMoney(long idTargetUser, double amount, String money, int typeMoney, String type, String token) {
        String noDisponible = " no esta disponible";
        long idUser = authService.getUserFromToken(token).getId();
        if (idTargetUser == idUser)
            throw new ExceptionCustom("Error no se puede enviar dinero al mismo usuario");

        Optional<User> user = userService.findById(idUser);
        if (user.isEmpty())
            throw new ExceptionCustom("El usuario con id " + idUser + noDisponible);

        Optional<User> targetUser = userService.findById(idTargetUser);
        if (targetUser.isEmpty())
            throw new ExceptionCustom("El usuario con id " + idTargetUser + noDisponible);

        Optional<Account> accountUser = accountService.findTopByUserId(idUser);
        if (accountUser.isEmpty())
            throw new ExceptionCustom("La account con id " + idUser + noDisponible);

        Optional<Account> accountTargetUser = accountService.findTopByUserId(idTargetUser);
        if (accountTargetUser.isEmpty())
            throw new ExceptionCustom("La account con id " + idTargetUser + noDisponible);

        validTypeOfMoney(typeMoney, money, accountUser.get(), accountTargetUser.get());

        if (accountUser.get().getBalance() < amount)
            throw new ExceptionCustom("Error valor disponible superado");

        if (amount > accountUser.get().getTransactionLimit())
            throw new ExceptionCustom("Error supera el limite de transacciones");

        double balanceUser = accountUser.get().getBalance() - amount;
        double targetUserBalance = accountTargetUser.get().getBalance() + amount;

        accountUser.get().setBalance((balanceUser));
        accountTargetUser.get().setBalance(targetUserBalance);

        Transaction transaction = new Transaction(null, amount, specificTypeOfTransaction(type), "Transacción exitosa",
                LocalDateTime.now(), targetUser.get(), accountUser.get());

        accountService.save(accountTargetUser.get());
        accountService.save(accountUser.get());
        repository.save(transaction);

        return Map.of("Mensaje","operación realizada exitosamente");
    }

    private void validTypeOfMoney(int typeMoney, String money, Account accountUser, Account accountTargetUser) {
        String error = "Error solo puede enviar dinero en ";
        if (typeMoney == 1 && (!accountUser.getCurrency().equals(ARS) || !accountTargetUser.getCurrency().equals(ARS)))
            throw new ExceptionCustom(error + money);
        else {
            if (typeMoney == 2 && (!accountUser.getCurrency().equals(USD) || !accountTargetUser.getCurrency().equals(USD)))
                throw new ExceptionCustom(error + money);
        }
    }

    private TransactionTypeEnum specificTypeOfTransaction(String type) {
        if (TransactionTypeEnum.PAYMENT.name().equalsIgnoreCase(type))
            return TransactionTypeEnum.PAYMENT;
        else if (TransactionTypeEnum.INCOME.name().equalsIgnoreCase(type))
            return TransactionTypeEnum.INCOME;
        else if (TransactionTypeEnum.DEPOSIT.name().equalsIgnoreCase(type))
            return TransactionTypeEnum.DEPOSIT;
        else
            throw new ExceptionCustom("El tipo ingresado es incorrecto");
    }
}
