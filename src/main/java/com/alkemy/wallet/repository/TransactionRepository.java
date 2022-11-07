package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.IBalance;
import com.alkemy.wallet.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query(value = "SELECT SUM(CASE WHEN T.TYPE = 'INCOME' THEN T.AMOUNT else 0 end) as sumIncome, SUM(CASE WHEN  T.TYPE = 'PAYMENT' THEN t.AMOUNT else 0 end) as sumPayment FROM TRANSACTIONS T WHERE T.ACCOUNT_ID = 1", nativeQuery=true)
    List<IBalance> getBalance(String id);
}
