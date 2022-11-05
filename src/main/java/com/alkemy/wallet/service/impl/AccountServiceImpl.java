package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.configuration.Authentication;
import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements IAccountService {

    private final BankDAO bankDAO;


    private static final Map<String, Double> TRANSACTION_LIMIT = new HashMap<>();
    static {
        TRANSACTION_LIMIT.put("ARS", 300000.0);
        TRANSACTION_LIMIT.put("USD", 1000.0);
    }


    @Override
    public void createAccount(AccountDTO account){
        account.setTransactionLimit(TRANSACTION_LIMIT.get(account.getCurrency()));
        UserEntity userEntity = bankDAO.findByEmail("pbmarin2015@gmail.com");
        AccountEntity accountEntity = bankDAO.createAccount(account, userEntity);
    }



    /*@Override
    public double getBalance() {
        return 0;
    }*/
}
