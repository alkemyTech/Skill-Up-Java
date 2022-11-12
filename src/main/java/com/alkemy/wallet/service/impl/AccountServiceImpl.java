package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.validator.IValidatorAccount;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.utils.DTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements IAccountService {
    private final BankDAO bankDAO;
    private static final Map<String, Double> TRANSACTION_LIMIT = new HashMap<>();

    static {
        TRANSACTION_LIMIT.put(ARS.getCurrency(), ARS.getAmount());
        TRANSACTION_LIMIT.put(USD.getCurrency(), USD.getAmount());
    }

    @Override
    public ResponseEntity<Object> createAccount(AccountDTO account) {
        DTOValidator.validate(account, IValidatorAccount.class);
        account.setTransactionLimit(TRANSACTION_LIMIT.get(account.getCurrency()));
        UserEntity userEntity = bankDAO.findUserByEmail("pbmarin2015@gmail.com");
        AccountEntity accountEntity = bankDAO.createAccount(account, userEntity);
        return new ResponseEntity<>("successfully created", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateAccountId(Long id, AccountDTO account) {
        DTOValidator.validate(account, IValidatorAccount.class);
        bankDAO.updateAccount(id, account);
        return new ResponseEntity<>("updated account", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(Long userId) {
        Optional<UserEntity> opUser = bankDAO.getUserById(userId);

        if(opUser.isEmpty()) {
            throw new BankException("The requested user Id does not exist");
        }
        return ResponseEntity.ok(bankDAO.getAllAccountByUser(opUser.get()));
    }

    @Override
    public ResponseEntity<Page<AccountEntity>> showAccountsPage(PageRequest pageRequest) {
        Page<AccountEntity> pageAccounts = bankDAO.showAccountsPage(pageRequest);

        if (pageAccounts.isEmpty()){
            throw new BankException("Missing page number");
        }

        return ResponseEntity.ok(pageAccounts);
    }

    @Override
    public void addNavigationAttributesToModel(int pageNumber, Model model, PageRequest pageRequest) {
        Page<AccountEntity> pageAccounts = bankDAO.showAccountsPage(pageRequest);

        int totalPages = pageAccounts.getTotalPages();
        if(totalPages > 0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
            model.addAttribute("current", pageNumber+1);
            model.addAttribute("next", pageNumber+2);
            model.addAttribute("prev", pageNumber);
            model.addAttribute("last", totalPages);
        }

        model.addAttribute("List", pageAccounts.getContent());
    }
}