package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedTermDepositRepository extends JpaRepository<FixedTermDeposit, Integer> {
    @Query("SELECT f FROM FixedTermDeposit f WHERE f.account = ?1")
    List<FixedTermDeposit> findallByAccount(Account account);
    List<FixedTermDeposit> findByAccount_AccountId(Integer accountId);

}
