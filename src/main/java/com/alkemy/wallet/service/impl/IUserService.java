package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import com.alkemy.wallet.model.mapper.response.UserListResponseDto;

public interface IUserService {

    UserResponseDto getUserById(Long id);

    UserListResponseDto getUsers();
}
