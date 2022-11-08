package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SendMoneyImpl {

    private String specificTypeOfMoney(int typeMoney, String money, Account accountUser, Account accountTargetUser) {
        String error = "Error solo puede enviar dinero en ";
        if (typeMoney == 1 && (!accountUser.getCurrency().equals(AccountCurrencyEnum.ARS) || !accountTargetUser.getCurrency().equals(AccountCurrencyEnum.ARS)))
            return error + money;
        else {
            if (typeMoney == 2 && (!accountUser.getCurrency().equals(AccountCurrencyEnum.USD) || !accountTargetUser.getCurrency().equals(AccountCurrencyEnum.USD)))
                return error + money;
        }
       return null;
    }

    public String sendMoney(Long idUser, Long idTargetUser, Double amount, String money,int typeMoney, ITransactionRepository
            repository, IUserRepository userRepository, IAccountRepository accountRepository) {

        String noDisponible = " no esta disponible";
        if (idTargetUser.equals(idUser))
            return "Error no se puede enviar dinero al mismo usuario";

        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty())
            return "El usuario con id " + idUser + noDisponible;

        Optional<User> targetUser = userRepository.findById(idTargetUser);
        if (targetUser.isEmpty())
            return "El usuario con id " + idTargetUser + noDisponible;

        Optional<Account> accountUser = accountRepository.findByFkUserId(idUser);
        if (accountUser.isEmpty())
            return "La account con id " + idUser + noDisponible;

        Optional<Account> accountTargetUser = accountRepository.findByFkUserId(idTargetUser);
        if (accountTargetUser.isEmpty())
            return "La account con id " + idTargetUser + noDisponible;

        String error = specificTypeOfMoney(typeMoney, money, accountUser.get(), accountTargetUser.get());
        if (error != null)
            return error;

        if (accountUser.get().getBalance() < amount)
            return "Error valor disponible superado";

        if (amount > accountUser.get().getTransactionLimit())
            return "Error supera el limite de transacciones";

        double balanceUser = accountUser.get().getBalance() - amount;
        double targetUserBalance = accountTargetUser.get().getBalance() + amount;

        accountUser.get().setBalance((balanceUser));
        accountTargetUser.get().setBalance(targetUserBalance);

        Transaction transaction = new Transaction();
        //null, amount, TransactionTypeEnum.INCOME,
         //       "Transacción exitosa", idUser, idTargetUser, LocalDateTime.now(), null, null);

        accountRepository.save(accountTargetUser.get());
        accountRepository.save(accountUser.get());
        repository.save(transaction);

        return "Operación realizada exitosamente";
    }
}
