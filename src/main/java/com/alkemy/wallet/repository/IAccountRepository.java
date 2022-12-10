package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//public interface IAccountRepository extends JpaRepository<Account, Long> {
//    HashSet<Account> findByUserId(Long user_id);

public interface IAccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAllByUser_Id(Long userId);

    List<Account> findAllByUser_Email(String email);

    Account findByCurrencyAndUser_Email(Currency currency, String email);
}
