package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.request.UserRequestDto;
import com.alkemy.wallet.model.response.UserResponseDto;
import com.alkemy.wallet.service.IUserService;

import com.alkemy.wallet.model.response.AccountResponseDto;
import com.alkemy.wallet.model.response.list.UserListResponseDto;
import com.alkemy.wallet.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        UserResponseDto response = service.getUserDetails(id, token);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateById(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody UserRequestDto request) {
        UserResponseDto response = service.update(id, token, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<UserListResponseDto> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }
}
