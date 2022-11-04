package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.user.AccountSinUserResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import com.alkemy.wallet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/accounts/{userId}")
    public ResponseEntity<AccountSinUserResponseDto> getAccountUserById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(service.getAccountUserById(userId), HttpStatus.OK);
    }
}
