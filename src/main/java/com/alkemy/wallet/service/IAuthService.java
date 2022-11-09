package com.alkemy.wallet.service;

import com.alkemy.wallet.model.request.AuthRequestDto;
import com.alkemy.wallet.model.request.UserRequestDto;
import com.alkemy.wallet.model.response.AuthResponseDto;
import com.alkemy.wallet.model.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;

public interface IAuthService {

    UserResponseDto register(UserRequestDto user);

    AuthResponseDto login(AuthRequestDto request);

    User getByEmail(String email);

    Role getRoleById(Long roleId);

    Role getRoleByName(String name);
}
