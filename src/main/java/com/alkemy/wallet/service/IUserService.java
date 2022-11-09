package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;

public interface IUserService {

    UserResponseDto getUserById(Long id);

    UserListResponseDto getUsers();
    UserResponseDto deleteUserById(Long id);

    AccountResponseDto getAccountUserById(Long userId);
}
