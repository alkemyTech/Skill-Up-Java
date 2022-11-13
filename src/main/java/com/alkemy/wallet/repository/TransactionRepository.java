package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Modifying
    @Transactional
    @Query("update Transaction u set u.description = :description where u.id = :id")
    void updateDescription(@Param(value = "id") Integer id, @Param(value = "description") String description);

    @Query("select t from Transaction t where t.account.user.id = ?1 order by t.transactionDate DESC")
    List<Transaction> findByUserId(Integer id);

    @Query("select t from Transaction t where t.account.id in ?1 order by t.transactionDate DESC")
    Page<Transaction> findByAccountIds(Collection<Integer> account_ids, Pageable pageable);

}
