package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT * FROM accounts WHERE user_id = :userId", nativeQuery = true)
    List<Account> findAccountsByUserId(Long userId);

    @Query(value = "SELECT * FROM accounts WHERE currency_type LIKE :currency AND user_id = :userId", nativeQuery = true)
    Optional<Account> findByCurrencyAndUserId(@Param("currency") String currency, @Param("userId") Long userId);
}