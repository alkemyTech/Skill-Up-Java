package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.service.IUserService;
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
        UserResponseDto response = service.getUserById(id, token);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<UserListResponseDto> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }
}
