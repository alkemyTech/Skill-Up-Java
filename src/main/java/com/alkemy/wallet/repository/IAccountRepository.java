package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    HashSet<Account> findByUserId(Long user_id);
}
