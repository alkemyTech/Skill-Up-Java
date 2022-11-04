package com.alkemy.wallet.model.mapper.response.account;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.dto.response.account.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Este es para mapear Account en otros mapper sin el error spring.main.allow-circular-references*/
@Mapper(componentModel = "spring")
public interface IAccountResponseMapper {
    @Mapping(target = "userId", source = "fkUserId")
    AccountResponseDto accountToAccountResponse(Account account);
}
