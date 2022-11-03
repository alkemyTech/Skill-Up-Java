package com.alkemy.wallet.model.mapper.response.account;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.mapper.response.complemento.IUserResponse2Mapper;
import com.alkemy.wallet.model.response.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IUserResponse2Mapper.class})
public interface IAccountResponseMapper {
    @Mapping(target = "userId", source = "fkUserId")
    @Mapping(target = "userResponseDto", source = "account.user")
    AccountResponseDto accountToAccountResponse(Account account);
}
