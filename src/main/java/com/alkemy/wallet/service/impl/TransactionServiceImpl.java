package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    TransactionRepository transactionRepository;
   // @Autowired
    //ModelMapper modelMapper;


    @Override
    public TransactionDTO getTransactionById(Integer id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        //TODO : ExceptionHandling if not exist.

        Transaction transEntity = optTrans.get();
       // TransactionDTO transDTO = modelMapper.map(transEntity , TransactionDTO.class);

        return null;
    }

    @Override
    public List<TransactionDTO> getAllByUserId(Integer id) {
        //TODO : Check if user:id is the same user logged.
        //if...
        Pageable pageWithTenElements = PageRequest.of(0, 10);
        //TODO : Modify mapper.
        //List<Transaction> allTransByUser = transactionRepository.findAllByUser(id, pageWithTenElements);
        //List<TransactionDTO> transList = transactionMapper.transListEntity2ListDTO(allTransByUser);
        return null;
    }

    @Override
    public void makeTransaction(TransactionCreateDTO transDTO) {
        //TODO :
        // -Modify mapper
        // -Validate transactionLimit (in Account)
        // -Modify balance amount (Check with Account team)
        // -, depends on TYPE transaction in DTO (Deposit,Payment and Income)
        // -, maybe i can make a "makePayment" method thats inherits from this (createTransaction)
        //Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        //transactionRepository.save(newTrans);
    }

    @Override
    public void updateTransaction(TransactionUpdateDTO transDTO, Integer id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        //TODO : Exception Handling
        //  - Modify mapper
        Transaction transEntity = optTrans.get();
        //Transaction transUpdate = transactionMapper.transUpdateDTO2Entity(transDTO, transEntity);
        //TODO : Check if save is necessary.
        //Transaction transUpdated = transactionRepository.save(transUpdate);
    }
}
