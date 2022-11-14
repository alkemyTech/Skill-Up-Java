package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.dto.response.list.TransactionListResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public Transaction dto2Entity(TransactionRequestDto dto, TransactionTypeEnum type, User user, Account account) {
        return Transaction.builder()
                .amount(dto.getAmount())
                .type(type)
                .description(dto.getDescription())
                .transactionDate(LocalDateTime.now())
                .user(user)
                .account(account)
                .build();
    }

    public List<TransactionResponseDto> entityList2DtoList(List<Transaction> entityList) {
        return entityList.stream().map(this::entity2Dto).collect(Collectors.toList());
    }
}