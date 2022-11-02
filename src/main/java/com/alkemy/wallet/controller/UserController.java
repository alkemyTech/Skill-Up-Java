package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping( "/users" )
class UserController {
    private final UserService service;

    @GetMapping
    List<UserDto> getAll() {
        return service.getAllUsers();
    }
}
