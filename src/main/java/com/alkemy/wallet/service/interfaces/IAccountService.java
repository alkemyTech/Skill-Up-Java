package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.Currency;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAccountService {

    AccountDto createAccount(Account account);

    List<AccountDto> getAccountsByUserId(Long userId);

    @Transactional(readOnly = true)
    List<AccountDto> getAccountsByUserEmail(String email) throws EmptyResultDataAccessException;

    AccountDto getAccountByCurrency(Long user_id, Currency currency);

    boolean checkAccountLimit(AccountDto senderAccount, TransactionDto destinedTransactionDto);

    AccountDto updateAccount(Long id, AccountUpdateDto newTransactionLimit);

    boolean checkAccountExistence(Long user_id, Currency currency);

}
