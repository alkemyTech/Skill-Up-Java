package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public Account dto2Entity(AccountRequestDto dto, AccountCurrencyEnum currency, Double transactionLimit, User user) {
        return Account.builder()
                .currency(currency)
                .transactionLimit(transactionLimit)
                .balance(0.0)
                .user(user)
                .creationDate(LocalDateTime.now())
                .build();
    }

    public List<AccountResponseDto> entityList2DtoList(List<Account> entityList) {
        return entityList.stream().map(this::entity2Dto).toList();
    }
}