package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


import java.util.HashSet;

//public interface IAccountRepository extends JpaRepository<Account, Long> {
//    HashSet<Account> findByUserId(Long user_id);

public interface IAccountRepository extends JpaRepository<Account,Long> {
    @Query(value= "SELECT * FROM account where user_id = ?1", nativeQuery = true)
    List<Account> getAccountsByUser(Long userId);
}
