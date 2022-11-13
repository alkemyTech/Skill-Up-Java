package com.alkemy.wallet.service.impl;

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
        String noDisponible = " not available";
        long idUser = authService.getUserFromToken(token).getId();
        if (idTargetUser == idUser)
            throw new IllegalArgumentException("Error cannot send money to the same user");

        Optional<User> user = userService.findById(idUser);
        if (user.isEmpty())
            throw new IllegalArgumentException(String.format("The user with id %d %s", idUser ,noDisponible));

        Optional<User> targetUser = userService.findById(idTargetUser);
        if (targetUser.isEmpty())
            throw new IllegalArgumentException("The user with id " + idTargetUser + noDisponible);

        Optional<Account> accountUser = accountService.findTopByUserId(idUser);
        if (accountUser.isEmpty())
            throw new IllegalArgumentException("the account with id " + idUser + noDisponible);

        Optional<Account> accountTargetUser = accountService.findTopByUserId(idTargetUser);
        if (accountTargetUser.isEmpty())
            throw new IllegalArgumentException("the account with id " + idTargetUser + noDisponible);

        validTypeOfMoney(typeMoney, money, accountUser.get(), accountTargetUser.get());

        if (accountUser.get().getBalance() < amount)
            throw new IllegalArgumentException("Available value exceeded error");

        if (amount > accountUser.get().getTransactionLimit())
            throw new IllegalArgumentException("Exceed transaction limit");

        double balanceUser = accountUser.get().getBalance() - amount;
        double targetUserBalance = accountTargetUser.get().getBalance() + amount;

        accountUser.get().setBalance((balanceUser));
        accountTargetUser.get().setBalance(targetUserBalance);

        Transaction transaction = new Transaction(null, amount, specificTypeOfTransaction(type), "Successful transaction",
                LocalDateTime.now(), targetUser.get(), accountUser.get());

        accountService.save(accountTargetUser.get());
        accountService.save(accountUser.get());
        repository.save(transaction);

        return Map.of("Message","operation performed successfully");
    }

    private void validTypeOfMoney(int typeMoney, String money, Account accountUser, Account accountTargetUser) {
        String error = "Error can only send money in ";
        if (typeMoney == 1 && (!accountUser.getCurrency().equals(ARS) || !accountTargetUser.getCurrency().equals(ARS)))
            throw new IllegalArgumentException(error + money);
        else {
            if (typeMoney == 2 && (!accountUser.getCurrency().equals(USD) || !accountTargetUser.getCurrency().equals(USD)))
                throw new IllegalArgumentException(error + money);
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
            throw new IllegalArgumentException("Wrong transaction type");
    }
}
