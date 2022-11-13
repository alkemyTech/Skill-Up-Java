package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.request.AccountRequestDto;
import com.alkemy.wallet.model.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.AccountCurrencyEnum;
import com.alkemy.wallet.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapper {

    public Account dto2Entity(AccountRequestDto dto, User user, AccountCurrencyEnum currency) {
        return Account.builder()
                .currency(currency)
                .transactionLimit(dto.getTransactionLimit())
                .balance(dto.getBalance())
                .creationDate(LocalDateTime.now())
                .user(user)
                .build();
    }

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
        List<AccountResponseDto> list = new ArrayList<>();
        entityList.forEach(account -> list.add(entity2Dto(account)));
        return list;
    }
}
