package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;

public interface IAuthService {

    UserResponseDto register(UserRequestDto userRequestDto);

    AuthResponseDto login(AuthRequestDto authRequestDto);

    String getEmailFromContext();

    String encode(String toEncode);

    String generateToken(String email);
}
