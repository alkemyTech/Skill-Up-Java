package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMapper {

    public AccountResponseDto entity2Dto(Account entity) {
        return AccountResponseDto.builder()
                .id(entity.getId())
                .currency(entity.getCurrency().name())
                .transactionLimit(entity.getTransactionLimit())
                .balance(entity.getBalance())
                .userId(entity.getUser().getId())
                .createdAt(entity.getCreationDate())
                .updatedAt(entity.getUpdateDate())
                .build();
    }

    public List<AccountResponseDto> entityList2DtoList(List<Account> entityList) {
        return entityList.stream().map(this::entity2Dto).toList();
    }
}