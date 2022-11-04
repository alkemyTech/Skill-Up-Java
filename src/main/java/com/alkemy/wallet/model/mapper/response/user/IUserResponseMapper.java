package com.alkemy.wallet.model.mapper.response.user;

import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Este es para mapear User en otros mapper sin el error spring.main.allow-circular-references */
@Mapper(componentModel = "spring")
public interface IUserResponseMapper {
    @Mapping(target = "roleId", source = "fkRoleId")
    UserResponseDto userToUserResponse(User user);
}
