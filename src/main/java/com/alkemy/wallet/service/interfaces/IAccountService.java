package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.BasicAccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.Currency;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAccountService {

    BasicAccountDto createAccount(Account account);

    List<Account> getAccountsByUserId(Long userId);

    @Transactional(readOnly = true)
    Page<AccountDto> findAllAccountsPageable(int page) throws EmptyResultDataAccessException;

    @Transactional(readOnly = true)
    List<BasicAccountDto> getAccountsByUserEmail(String email) throws EmptyResultDataAccessException;

    Account getAccountByCurrency(Long user_id, Currency currency);

    boolean checkAccountLimit(Account senderAccount, TransactionDto transactionDto);

    ResponseEntity<?> updateAccount(Long id, AccountUpdateDto newTransactionLimit, String token);

    boolean checkAccountExistence(Long user_id, Currency currency);

    ResponseEntity<?> postAccount(BasicAccountDto basicAccountDto, String token);
}
