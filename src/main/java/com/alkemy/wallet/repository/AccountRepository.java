package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
   @Query("SELECT a FROM Account a WHERE a.user = ?1 AND a.currency = ?2")
   Optional<Account> findAccountByUserIdAndCurrency(User user, Currency currency);

   @Query("SELECT a FROM Account a WHERE a.user = ?1")
   List<Account> findAccountsByUserId(User user);

   @Query("SELECT a FROM Account a WHERE a.user = ?1")
   Page<Account> findAccountsByUserId(User user, Pageable pageable);
}
