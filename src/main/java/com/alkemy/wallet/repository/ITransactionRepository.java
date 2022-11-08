package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "select * from transactions where fk_user_id = :userId", nativeQuery = true)
    public List<Transaction> getByUserId(Long userId);

}
