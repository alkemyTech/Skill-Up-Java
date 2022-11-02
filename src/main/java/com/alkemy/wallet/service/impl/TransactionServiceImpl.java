package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionMapper transactionMapper;


    @Override
    public TransactionDTO getById(Integer id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        //TODO : ExceptionHandling if not exist.

        Transaction transEntity = optTrans.get();
        TransactionDTO transDTO = transactionMapper.transEntity2DTO(transEntity);

        return transDTO;
    }

    @Override
    public List<TransactionDTO> getAllByUser(Integer id) {
        //TODO : Check if user:id is the same user logged.
        //if...
        Pageable pageWithTenElements = PageRequest.of(0, 10);
        //TODO : Talk with DB team (findAllByUser method).
        List<Transaction> allTransByUser = transactionRepository.findAllByUser(id, pageWithTenElements);
        List<TransactionDTO> transList = transactionMapper.transListEntity2ListDTO(allTransByUser);
        return transList;
    }

    @Override
    public void createTransaction(TransactionCreateDTO transDTO) {
        //TODO :
        // -Validate transactionLimit (in Account)
        // -Modify balance amount (Check with Account team)
        // -, depends on TYPE transaction in DTO (Deposit,Payment and Income)
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        transactionRepository.save(newTrans);
    }

    @Override
    public void updateTransaction(TransactionUpdateDTO transDTO, Integer id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        //TODO : Exception Handling
        Transaction transEntity = optTrans.get();
        Transaction transUpdate = transactionMapper.transUpdateDTO2Entity(transDTO, transEntity);
        //TODO : Check if save is necessary.
        Transaction transUpdated = transactionRepository.save(transUpdate);
    }
}
