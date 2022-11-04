package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
}
