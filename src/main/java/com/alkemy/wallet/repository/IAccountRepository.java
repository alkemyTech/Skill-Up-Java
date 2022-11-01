package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT * FROM Account a WHERE a.userId = ?1 AND a.currency = ?2")
    boolean existAccountByUserIdAndCurrency(int userId, Currency currency);
}
