package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
}
