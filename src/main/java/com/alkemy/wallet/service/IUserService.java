package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.User;
import java.util.Optional;
import com.alkemy.wallet.model.request.UserRequestDto;
import com.alkemy.wallet.model.response.UserResponseDto;
import com.alkemy.wallet.model.response.list.UserListResponseDto;

public interface IUserService {

    UserResponseDto update(Long id, String token, UserRequestDto request);

    User getEntityById(Long id);

    UserResponseDto getUserDetails(Long id, String token);

    UserListResponseDto getUsers();
    UserResponseDto deleteUserById(Long id);
    User save(User accountUser);
    Optional<User> findById(Long id);
}