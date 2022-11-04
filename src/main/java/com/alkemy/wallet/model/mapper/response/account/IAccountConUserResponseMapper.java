package com.alkemy.wallet.model.mapper.response.account;

import com.alkemy.wallet.model.dto.response.account.AccountConUserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.mapper.response.user.IUserResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IUserResponseMapper.class})
public interface IAccountConUserResponseMapper {
    @Mapping(target = "userId", source = "fkUserId")
    @Mapping(target = "userResponseDto", source = "account.user")
    AccountConUserResponseDto accountToAccountConUseResponse(Account account);
}
