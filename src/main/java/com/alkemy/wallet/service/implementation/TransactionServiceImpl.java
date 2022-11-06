package com.alkemy.wallet.service.implementation;


import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.dto.TransactionPatchDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public TransactionDetailDto getTransactionDetailById(Integer Id) throws ResourceNotFoundException {
        var transaction = transactionRepository.findById(Id);
        if(transaction.isPresent()){
            return transactionMapper.convertToTransactionDetailDto(transaction.get());
        }else{
            throw new ResourceNotFoundException("Transaction does not exist");
        }
    }

    @Override
    public TransactionDepositDto createDeposit(TransactionDepositDto transaction) {
        Double amount = transaction.getAmount();

        // It would be nice to have an exception handler. We should implement it in a separate branch
        if(amount <= 0) {
            throw new InvalidAmountException();
        }
        Transaction transactionCreated = transactionRepository.save(transactionMapper.convertToEntity(transaction));
        return transactionMapper.convertToTransactionDepositDto(transactionCreated);
    }

    @Override
    public List<TransactionDetailDto> getTransactions(Integer userId) {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> transactionsOfUser = transactions
                .stream()
                .filter(transaction ->
                        transaction.getAccount().getUser().getUserId().equals(userId))
                .toList();

        return convertTransactionListToDto(transactionsOfUser);

    }

    private List<TransactionDetailDto> convertTransactionListToDto(List<Transaction> transactions){
        return transactions
                .stream()
                .map(transaction -> transactionMapper.convertToTransactionDetailDto(transaction))
                .toList();
    }

    @Override
    public TransactionDetailDto updateTransaction(TransactionPatchDto transactionPatch, Integer Id) throws Exception {
        var transaction = transactionRepository.findById(Id).orElseThrow(Exception::new);
        transactionMapper.transactionPatchToTransaction(transactionPatch);
        return transactionMapper.convertToDto(transactionRepository.save(transaction));
    }
}