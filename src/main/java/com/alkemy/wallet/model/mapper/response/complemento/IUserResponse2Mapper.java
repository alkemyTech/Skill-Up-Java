package com.alkemy.wallet.model.mapper.response.complemento;

import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Este es para mapear User en otros mapper sin el error spring.main.allow-circular-references */
@Mapper(componentModel = "spring")
public interface IUserResponse2Mapper {
    @Mapping(target = "roleId", source = "fkRoleId")
    @Mapping(target = "accountResponseDto", ignore = true)
    UserResponseDto userToUserResponse(User user);
}
