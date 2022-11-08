package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUser(User userId);
    Account findByCurrency(String currency);
    @Query(value = "SELECT * FROM accounts t WHERE t.user_id = ?1 ;", nativeQuery = true)
    List<Account> findByUserID(Integer user_id);
}
