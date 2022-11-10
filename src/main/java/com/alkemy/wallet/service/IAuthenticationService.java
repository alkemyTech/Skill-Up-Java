package com.alkemy.wallet.service;

import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAuthenticationService {
    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest authenticationRequest);


}
