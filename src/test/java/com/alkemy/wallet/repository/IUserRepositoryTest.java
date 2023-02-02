package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.constant.AccountCurrencyEnum;
import com.alkemy.wallet.model.constant.RoleEnum;
import com.alkemy.wallet.model.constant.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.*;
import com.alkemy.wallet.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@DataJpaTest
class IUserRepositoryTest {

    @Mock
    protected IAccountRepository accountRepository;
    @Mock
    protected IRoleRepository roleRepository;
    @Mock
    protected ITransactionRepository transactionRepository;
    @Mock
    protected IFixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    protected IUserRepository underTest;

    protected Role ROLE_ADMIN;
    protected Role ROLE_USER;
    protected User user1;
    protected User user2;
    protected List<Account> accountsUser1;
    protected List<Account> accountsUser2;
    protected List<Transaction> transactionsUser1;
    protected List<Transaction> transactionsUser2;
    protected List<FixedTermDeposit> fixedTermDepositsUser1;
    protected List<FixedTermDeposit> fixedTermDepositsUser2;

    @BeforeEach
    void setUp() {
        // Instantiating users
        user1 = new User();
        user1.setFirstName("Hector");
        user1.setLastName("Cortez");
        user1.setEmail("hector@gmail.com");
        user1.setPassword("password");
        user1.setUpdateDate(null);
        user1.setFixedTermDeposits(null);

        user2 = new User();
        user2.setFirstName("Francisco");
        user2.setLastName("Orieta");
        user2.setEmail("fran@gmail.com");
        user2.setPassword("password123");
        user2.setUpdateDate(null);
        user2.setFixedTermDeposits(null);

        // Instantiating roles
        ROLE_ADMIN = new Role();
        ROLE_ADMIN.setName(RoleEnum.ADMIN);
        ROLE_ADMIN.setCreationDate(LocalDateTime.now());
        ROLE_ADMIN.setUpdateDate(null);
        ROLE_ADMIN.setUser(user1);

        ROLE_USER = new Role();
        ROLE_USER.setName(RoleEnum.USER);
        ROLE_USER.setCreationDate(LocalDateTime.now());
        ROLE_USER.setUpdateDate(null);
        ROLE_USER.setUser(user2);

        // Saving roles
        given(roleRepository.save(ROLE_ADMIN)).willReturn(ROLE_ADMIN);
        given(roleRepository.save(ROLE_USER)).willReturn(ROLE_USER);

        // Setting up roles to each user
        user1.setAuthorities(Collections.singleton(ROLE_ADMIN));
        user2.setAuthorities(Collections.singleton(ROLE_USER));

        // Instantiating accounts
        Account account1, account2, account3, account4;
        account1 = new Account();
        account1.setCurrency(AccountCurrencyEnum.ARS);
        account1.setTransactionLimit(300000.0);
        account1.setBalance(0.0);
        account1.setUpdateDate(null);
        account1.setUser(user1);
        account1.setTransactions(null);
        account1.setFixedTermDeposits(null);

        account2 = new Account();
        account2.setCurrency(AccountCurrencyEnum.USD);
        account2.setTransactionLimit(1000.0);
        account2.setBalance(0.0);
        account2.setUpdateDate(null);
        account2.setUser(user1);
        account2.setTransactions(null);
        account2.setFixedTermDeposits(null);

        account3 = new Account();
        account3.setCurrency(AccountCurrencyEnum.ARS);
        account3.setTransactionLimit(300000.0);
        account3.setBalance(0.0);
        account3.setUpdateDate(null);
        account3.setUser(user2);
        account3.setTransactions(null);
        account3.setFixedTermDeposits(null);

        account4 = new Account();
        account4.setCurrency(AccountCurrencyEnum.USD);
        account4.setTransactionLimit(1000.0);
        account4.setBalance(0.0);
        account4.setUpdateDate(null);
        account4.setUser(user2);
        account4.setTransactions(null);
        account4.setFixedTermDeposits(null);

        // Saving accounts
        given(accountRepository.save(account1)).willReturn(account1);
        given(accountRepository.save(account2)).willReturn(account2);
        given(accountRepository.save(account3)).willReturn(account3);
        given(accountRepository.save(account4)).willReturn(account4);

        // Instantiating transactions
        Transaction transaction1, transaction2, transaction3, transaction4;
        transaction1 = new Transaction();
        transaction1.setAmount(100000.0);
        transaction1.setType(TransactionTypeEnum.DEPOSIT);
        transaction1.setDescription("asset deposit");
        transaction1.setUser(user1);
        transaction1.setAccount(account1);

        transaction2 = new Transaction();
        transaction2.setAmount(10000.0);
        transaction2.setType(TransactionTypeEnum.INCOME);
        transaction2.setDescription("overtime deposit");
        transaction2.setUser(user1);
        transaction2.setAccount(account1);

        transaction3 = new Transaction();
        transaction3.setAmount(100000.0);
        transaction3.setType(TransactionTypeEnum.DEPOSIT);
        transaction3.setDescription("deposit check");
        transaction3.setUser(user2);
        transaction3.setAccount(account3);

        transaction4 = new Transaction();
        transaction4.setAmount(5000.0);
        transaction4.setType(TransactionTypeEnum.INCOME);
        transaction4.setDescription("game sale");
        transaction4.setUser(user2);
        transaction4.setAccount(account3);

        // Saving transactions
        given(transactionRepository.save(transaction1)).willReturn(transaction1);
        given(transactionRepository.save(transaction2)).willReturn(transaction2);
        given(transactionRepository.save(transaction3)).willReturn(transaction3);
        given(transactionRepository.save(transaction4)).willReturn(transaction4);

        // Instantiating FixedTermDeposits
        FixedTermDeposit fixedTermDeposit1, fixedTermDeposit2;
        fixedTermDeposit1 = new FixedTermDeposit();
        fixedTermDeposit1.setAmount(10000.0);
        fixedTermDeposit1.setInterest(15000.0);
        fixedTermDeposit1.setClosingDate(DateUtil.string2LocalDate("2027/02/26"));
        fixedTermDeposit1.setAccount(account1);
        fixedTermDeposit1.setUser(user1);

        fixedTermDeposit2 = new FixedTermDeposit();
        fixedTermDeposit2.setAmount(10000.0);
        fixedTermDeposit2.setInterest(15000.0);
        fixedTermDeposit2.setClosingDate(DateUtil.string2LocalDate("2027/02/26"));
        fixedTermDeposit2.setAccount(account3);
        fixedTermDeposit2.setUser(user2);

        // Saving FixedTermDeposits
        given(fixedTermDepositRepository.save(fixedTermDeposit1)).willReturn(fixedTermDeposit1);
        given(fixedTermDepositRepository.save(fixedTermDeposit2)).willReturn(fixedTermDeposit2);

        // Init lists for User One
        accountsUser1 = new ArrayList<>();
        accountsUser1.add(account1);
        accountsUser1.add(account2);

        transactionsUser1 = new ArrayList<>();
        transactionsUser1.add(transaction1);
        transactionsUser1.add(transaction2);

        fixedTermDepositsUser1 = new ArrayList<>();
        fixedTermDepositsUser1.add(fixedTermDeposit1);

        // Init lists for User Two
        accountsUser2 = new ArrayList<>();
        accountsUser2.add(account3);
        accountsUser2.add(account4);

        transactionsUser2 = new ArrayList<>();
        transactionsUser2.add(transaction3);
        transactionsUser2.add(transaction4);

        fixedTermDepositsUser2 = new ArrayList<>();
        fixedTermDepositsUser2.add(fixedTermDeposit2);

        //Setting up lists for each User
        user1.setAccounts(accountsUser1);
        user1.setTransactions(transactionsUser1);
        user1.setFixedTermDeposits(fixedTermDepositsUser1);

        user2.setAccounts(accountsUser2);
        user2.setTransactions(transactionsUser2);
        user2.setFixedTermDeposits(fixedTermDepositsUser2);
    }

    @Test
    void itShouldSCheckIfEmailExists() {
        // Given
        String emailThatExist = user1.getEmail();
        underTest.save(user1);
        underTest.save(user2);

        // When
        boolean expected = underTest.selectExistsEmail(emailThatExist);

        // Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldSCheckIfEmailDoesNotExists() {
        // Given
        String emailThatDoesNotExist = "example@gmail.com";
        underTest.save(user1);
        underTest.save(user2);

        // When
        boolean expected = underTest.selectExistsEmail(emailThatDoesNotExist);

        // Then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldCanGetUserByEmail() {
        // Given
        String emailThatExist = user2.getEmail();
        underTest.save(user1);
        underTest.save(user2);

        // When
        Optional<User> userOptional = underTest.findByEmail(emailThatExist);

        // Then
        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional).hasValueSatisfying(
                userFromDataBase -> assertThat(userFromDataBase).usingRecursiveComparison().isEqualTo(user2)
        );
    }

    @Test
    void itShouldNotCanGetUserByEmail() {
        // Given
        String emailThatDoesNotExist = "does_not_exist@gmail.com";
        underTest.save(user1);
        underTest.save(user2);

        // When
        Optional<User> userOptional = underTest.findByEmail(emailThatDoesNotExist);

        // Then
        assertThat(userOptional.isPresent()).isFalse();
        assertThat(userOptional).isEqualTo(Optional.empty());
    }
}