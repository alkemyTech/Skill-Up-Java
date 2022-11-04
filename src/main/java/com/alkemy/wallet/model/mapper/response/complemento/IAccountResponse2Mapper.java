package com.alkemy.wallet.model.mapper.response.complemento;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Este es para mapear Account en otros mapper sin el error spring.main.allow-circular-references */
@Mapper(componentModel = "spring")
public interface IAccountResponse2Mapper {
    @Mapping(target = "userId", source = "fkUserId")
    @Mapping(target = "userResponseDto", ignore = true)
    AccountResponseDto accountToAccountResponse(Account account);
}
