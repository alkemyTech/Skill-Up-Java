package com.alkemy.wallet.service;

import com.alkemy.wallet.model.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    private final ITransactionRepository repository;
    private final IUserRepository userRepository;

    private final IAccountRepository accountRepository;

    public TransactionService(ITransactionRepository repository, IUserRepository userRepository, IAccountRepository accountRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public String moneySendInPesos(Long idUser, Long idTargetUser, Double amount) {
        if (idTargetUser.equals(idUser))
            return "Error no se puede hacer una transferencia al mismo usuario";

        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty())
            return "El usuario con id " + idUser + " no esta disponible";

        Optional<User> targetUser = userRepository.findById(idTargetUser);
        if (targetUser.isEmpty())
            return "El usuario con id " + idTargetUser + " no esta disponible";

        Optional<Account> accountUser = accountRepository.findByFkUserId(idUser);
        if (accountUser.isEmpty())
            return "La account con id " + idUser + " no esta disponible";

        Optional<Account> accountTargetUser = accountRepository.findByFkUserId(idTargetUser);
        if (accountTargetUser.isEmpty())
            return "La account con id " + idTargetUser + " no esta disponible";

        if (accountUser.get().getBalance() < amount)
            return "Error valor disponible superado";

        if (amount > accountUser.get().getTransactionLimit())
            return "Error supera el limite de transacciones";

        double balanceUser = accountUser.get().getBalance() - amount;
        double targetUserBalance = accountTargetUser.get().getBalance() + amount;

        accountUser.get().setBalance((balanceUser));
        accountTargetUser.get().setBalance(targetUserBalance);

        Transaction transaction = new Transaction(null, amount, TransactionTypeEnum.INCOME,
                "Transacción exitosa", idUser, idTargetUser, LocalDateTime.now(), null, null);

        accountRepository.save(accountTargetUser.get());
        accountRepository.save(accountUser.get());
        repository.save(transaction);

        return "Operación realizada exitosamente";
    }
}
