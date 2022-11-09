package com.alkemy.wallet.service;

import com.alkemy.wallet.model.response.UserResponseDto;
import com.alkemy.wallet.model.response.list.UserListResponseDto;

public interface IUserService {

    UserResponseDto getUserById(Long id);

    UserListResponseDto getUsers();
}
