package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;

public interface IAuthService {

    UserResponseDto register(UserRequestDto userRequest);

    AuthResponseDto login(AuthRequestDto authRequest);
}
