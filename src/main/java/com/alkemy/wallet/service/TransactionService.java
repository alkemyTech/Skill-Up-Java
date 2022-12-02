package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    Mapper mapper;

    @Autowired
    ITransactionRepository transactionRepository;

    //    public TransactionDto save(@Valid TransactionDto transactionDto) {
//        Transaction transaction = mapper.getMapper().map(transactionDto, Transaction.class);
//        transactionRepository.save(transaction);
//        return transactionDto;
//    }

    /*public HashSet<TransactionDto> getByUserId(@Valid List<AccountDto> accounts) {

        List<Long> accounts_id = new ArrayList<>();
        for (AccountDto accountDto : accounts) {
            accounts_id.add(mapper.getMapper().map(accountDto, Long.class));
        }

        return transactionRepository.findByClientAccounts(accounts_id).stream().map((transaction) ->
                        mapper.getMapper().map(transaction, TransactionDto.class))
                .collect(Collectors.toCollection(HashSet::new));
    }*/
}
