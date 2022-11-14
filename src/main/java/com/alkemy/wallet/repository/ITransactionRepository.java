package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transactions WHERE user_id = :userId", nativeQuery = true)
    List<Transaction> findTransactionsByUserId(@Param("userId") Long id);
}