package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;

public interface UserService {
    UserDto createUser( UserRequestDto userRequestDto );
}
