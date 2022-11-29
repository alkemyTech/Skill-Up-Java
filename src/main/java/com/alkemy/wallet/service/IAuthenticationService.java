package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;

public interface IAuthenticationService {

    UserResponseDto register(UserRequestDto user);

    AuthResponseDto login(AuthRequestDto request);

    String getEmailFromContext();

    String encode(String toEncode);

    String generateToken(String userRequest);
}
