package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findTopByUserId(Long userId);

    @Query(value = "SELECT * FROM accounts WHERE user_id = :userId", nativeQuery = true)
    List<Account> findAccountByUserId(Long userId);
}
