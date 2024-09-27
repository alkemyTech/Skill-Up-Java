package com.alkemy.wallet.service;

import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationService {
    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest authenticationRequest);
}
