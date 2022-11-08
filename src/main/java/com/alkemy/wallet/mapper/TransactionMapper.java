package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    @Autowired
    private ModelMapper mapper;

    public TransactionDetailDto convertToTransactionDetailDto(Transaction transaction){
        return mapper.map( transaction, TransactionDetailDto.class);
    }

    public TransactionDepositDto convertToTransactionDepositDto(Transaction transaction){
        return mapper.map( transaction, TransactionDepositDto.class);
    }

    public TransactionDetailDto transactionPatchToTransaction(TransactionPatchDto transactionPatchDto) {
        return mapper.map(transactionPatchDto,TransactionDetailDto.class);
    }

    public TransactionPaymentDto convertToTransactionPaymentDto(Transaction transaction) {
        return mapper.map( transaction, TransactionPaymentDto.class);
    }

    public Transaction convertToEntity( TransactionDetailDto transactionDetailDto){
        return mapper.map( transactionDetailDto, Transaction.class);
    }

    public Transaction convertToEntity( TransactionDepositDto transactionDepositDto){
        return mapper.map( transactionDepositDto, Transaction.class);
    }

    public Transaction convertToEntity( TransactionTransferRequestDto transactionTransferRequestDto){
        return mapper.map( transactionTransferRequestDto, Transaction.class);
    }

    public Transaction convertToEntity( TransactionPaymentDto transactionPaymentDto){
        return mapper.map( transactionPaymentDto, Transaction.class);
    }

    public Transaction convertToEntity( TransactionPaymentRequestDto transactionPaymentDto){
        return mapper.map( transactionPaymentDto, Transaction.class);
    }
    public Transaction convertToEntity( TransactionIncomeRequestDto transactionIncomeRequestDto){
        return mapper.map( transactionIncomeRequestDto, Transaction.class);
    }
    public Transaction convertToEntity( TransactionIncomeDto transactionIncomeDto){
        return mapper.map( transactionIncomeDto, Transaction.class);
    }

    public TransactionDetailDto convertPaymentDtoToDetailDto( TransactionPaymentDto transactionPaymentDto){
        return mapper.map( transactionPaymentDto, TransactionDetailDto.class);
    }
}
