package com.alkemy.wallet.model.mapper.response.user;

import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.response.complemento.IAccountResponse2Mapper;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IAccountResponse2Mapper.class})
public interface IUserResponseMapper {
    @Mapping(target = "roleId", source = "fkRoleId")
    @Mapping(target = "accountResponseDto", source = "accounts")
    UserResponseDto userToUserResponse(User user);
}
