package com.alkemy.wallet.model.mapper.response.user;

import com.alkemy.wallet.model.dto.response.user.AccountSinUserResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserConAccountResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.response.account.IAccountResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IAccountResponseMapper.class})
public interface IUserConAccountResponseMapper {
    @Mapping(target = "roleId", source = "fkRoleId")
    @Mapping(target = "accountResponseDto", source = "accounts")
    UserConAccountResponseDto userToUserConAccountResponse(User user);

    @Mapping(target = "accountResponseDto", source = "accounts")
    AccountSinUserResponseDto userToAccountSinUserResponse(User user); /** nose que nombre ponerle (es una interfaz apenas con ese metodo)*/

    List<UserResponseDto> listUserToListUserResponse(List<User> users);
}
