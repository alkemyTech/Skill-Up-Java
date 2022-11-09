package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @GetMapping
    public ResponseEntity<UserListResponseDto> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    //TODO move this endpoint to AccountController
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<AccountResponseDto> getAccountUserById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(service.getAccountUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> deleteUserById(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(service.deleteUserById(userId), HttpStatus.OK);
    }
}
