package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface ITransactionRepository extends PagingAndSortingRepository<Transaction, Long>, JpaRepository<Transaction, Long> {
    //HashSet<Transaction> findByAccountId(Long account_id);

    HashSet<Transaction> findByAccount_idIn(List<Long> account_id);

    Page<Transaction> findByAccount_User_Id(Long id, Pageable pageRequest);


}
