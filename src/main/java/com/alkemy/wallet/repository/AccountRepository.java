package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
   @Query("SELECT a.accountId FROM Account a WHERE a.user = ?1 AND a.currency = ?2")
   Optional<Account> findAccountByUserIdAndCurrency(User user, Currency currency);
}
