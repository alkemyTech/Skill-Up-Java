package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    public TransactionResponseDto entity2Dto(Transaction entity) {
        return TransactionResponseDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .type(entity.getType().name())
                .description(entity.getDescription())
                .userId(entity.getUser().getId())
                .accountId(entity.getAccount().getId())
                .transactionDate(entity.getTransactionDate())
                .build();
    }

    public List<TransactionResponseDto> entityList2DtoList(List<Transaction> entityList) {
        return entityList.stream().map(this::entity2Dto).collect(Collectors.toList());
    }
}