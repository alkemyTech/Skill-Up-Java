package com.alkemy.wallet.service.implementation;


import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.InvalidAmountException;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return transactionMapper.convertToDto(transaction.get());
        }else{
            throw new ResourceNotFoundException("Transaction does not exist");
        }
    }

    @Override
    public Transaction createDeposit(Transaction transaction) {
        Double amount = transaction.getAmount();

        // It would be nice to have an exception handler. We should implement it in a separate branch
        if(amount <= 0) {
            throw new InvalidAmountException();
        }

        return transactionRepository.save(transaction);
    }
}