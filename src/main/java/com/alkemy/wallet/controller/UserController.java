package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl service;

    @GetMapping
    public ResponseEntity<UserListResponseDto> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }
}
