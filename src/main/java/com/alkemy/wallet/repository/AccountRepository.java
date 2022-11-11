package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("SELECT a FROM AccountEntity a WHERE a.user.userId = :userId AND a.currency = :currency")
    AccountEntity getAccount(@Param("userId") Long userId, @Param("currency") String currency);
}
