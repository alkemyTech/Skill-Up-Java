package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.TransactionDetailDto;
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

    public TransactionDetailDto convertToDto(Transaction transaction){
        return mapper.map( transaction, TransactionDetailDto.class);
    }

    public Transaction convertToEntity( TransactionDetailDto transactionDetailDto){
        return mapper.map( transactionDetailDto, Transaction.class);

  }
}
