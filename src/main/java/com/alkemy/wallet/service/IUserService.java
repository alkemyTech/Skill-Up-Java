package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.model.entity.User;

public interface IUserService {

    UserResponseDto update(Long id, String token, UserRequestDto request);

    User getEntityById(Long id);

    UserResponseDto getUserDetails(Long id, String token);

    UserListResponseDto getUsers();
    UserResponseDto deleteUserById(Long id);

}