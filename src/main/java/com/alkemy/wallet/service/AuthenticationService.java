package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRegisteredDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.security.AuthenticationRequest;
import com.alkemy.wallet.security.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<UserRegisteredDto> registerUser(UserRequestDto user);
    ResponseEntity<AuthenticationResponse> loginUser(AuthenticationRequest authenticationRequest);
}
