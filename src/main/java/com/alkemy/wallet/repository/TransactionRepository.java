package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.account = ?1")
    List<Transaction> findAllByAccountId(Account account);

    List<Transaction> findByAccount_User_UserId(Integer userId, Pageable pageable);


}
