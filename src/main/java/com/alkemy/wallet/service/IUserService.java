package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.model.entity.User;

import java.util.Optional;

public interface IUserService {

    UserResponseDto update(Long id, String token, UserRequestDto request);

    User getEntityById(Long id);

    UserResponseDto getUserDetails(Long id, String token);

    UserListResponseDto getUsers();
    UserResponseDto deleteUserById(Long id);
    User save(User accountUser);
    Optional<User> findById(long id);
}