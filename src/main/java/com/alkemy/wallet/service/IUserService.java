package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;

public interface IUserService {

    UserResponseDto getUserById(Long id, String token);

    UserListResponseDto getUsers();
}
