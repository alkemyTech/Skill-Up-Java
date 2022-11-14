/*
package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.*;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    IAuthService authService;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ITransactionService transactionService;

    @Autowired
    IAccountRepository accountRepository;

    private Transaction transaction;
    private Long idTransaction;

    @Before
    public void setUp() {
        Optional<User> optionalUser = userRepository.findByEmail("juanjo@gmail.com");

        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setFirstName("Juan");
            user.setLastName("Perez");
            user.setEmail("juanjo@gmail.com");
            user.setPassword("123456");
            user.setCreationDate(LocalDateTime.now());

            optionalUser = Optional.of(userRepository.save(user));
        }

        Account account = new Account();
        account.setCurrency(AccountCurrencyEnum.ARS);
        account.setTransactionLimit(1000.0);
        account.setBalance(100.0);
        account.setUser(optionalUser.get());

        transaction = new Transaction();
        transaction.setAmount(450.0);
        transaction.setType(TransactionTypeEnum.PAYMENT);
        transaction.setDescription("pago de servicio");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setUser(optionalUser.get());
        transaction.setAccount(accountRepository.save(account));
        idTransaction = transactionService.saveTransaction(transaction).getId();
    }

    @Test
    public void saveTransaction() {
        Optional<Transaction> optionalTransaction = transactionService.getTransactionById(idTransaction);
        Transaction loadedTransaction = optionalTransaction.get();

        assertEquals(transaction.getId(), loadedTransaction.getId());
        assertEquals(transaction.getAmount(), loadedTransaction.getAmount());
        assertEquals(transaction.getType(), loadedTransaction.getType());
        assertEquals(transaction.getDescription(), loadedTransaction.getDescription());
        assertEquals(transaction.getUser().getId(), loadedTransaction.getUser().getId());
        assertEquals(transaction.getAccount().getId(), loadedTransaction.getAccount().getId());

        transactionService.deleteTransaction(loadedTransaction.getId());
        accountRepository.deleteById(transaction.getAccount().getId());
    }
}
*/
