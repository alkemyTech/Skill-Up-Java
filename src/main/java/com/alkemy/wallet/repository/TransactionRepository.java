package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query(value = "SELECT A.CURRENCY, SUM(T.AMOUNT) AS AMOUNT, T.TYPE FROM ACCOUNTS A JOIN TRANSACTIONS T ON A.ACCOUNT_ID = T.ACCOUNT_ID WHERE A.ACCOUNT_ID = 1 GROUP BY T.type, A.CURRENCY", nativeQuery=true)
    List<IBalance> getBalance(@Param("userId") Long userId);
}
