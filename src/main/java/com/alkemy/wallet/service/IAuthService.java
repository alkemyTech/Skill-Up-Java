package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;

public interface IAuthService {

    UserResponseDto register(UserRequestDto user);

    AuthResponseDto login(AuthRequestDto request);

    User getByEmail(String email);

    User getUserFromToken(String token);

    Role getRoleById(Long roleId);

    String encode(String toEncode);
}
