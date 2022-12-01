package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    HashSet<Transaction> findByUserId(Long user_id);
}
