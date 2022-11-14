package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService service;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> signUp(@Validated @RequestBody UserRequestDto request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Validated @RequestBody AuthRequestDto request) {
        return ResponseEntity.ok().body(service.login(request));
    }
}