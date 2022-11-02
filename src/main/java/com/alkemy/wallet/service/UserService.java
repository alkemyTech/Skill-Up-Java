package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto createUser( UserRequestDto userRequestDto );
}
