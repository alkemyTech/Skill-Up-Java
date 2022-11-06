package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Object> createAccount(AccountDTO account){
        account.setTransactionLimit(TRANSACTION_LIMIT.get(account.getCurrency()));
        UserEntity userEntity = bankDAO.findByEmail("pbmarin2015@gmail.com");
        AccountEntity accountEntity = bankDAO.createAccount(account, userEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
