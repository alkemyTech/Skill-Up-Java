package com.alkemy.wallet.model.mapper.response.role;

import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.response.RoleResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRoleResponseMapper {
    RoleResponseDto roleToRoleResponse(Role role);
}
