package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface IAccountService {
   ResponseEntity<Object> createAccount(AccountDTO account);
   ResponseEntity<Object> updateAccountId(Long id, AccountDTO account);
   ResponseEntity<Page<AccountEntity>> showAccountsPage(PageRequest pageRequest);
   void addNavigationAttributesToModel(int pageNumber, Model model, PageRequest pageRequest);
}
