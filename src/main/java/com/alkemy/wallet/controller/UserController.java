package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.service.IUserService;

import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        UserResponseDto response = service.getUserDetails(id, token);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @Validated @RequestBody UserRequestDto request) {
        UserResponseDto response = service.update(id, token, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        service.deleteUserById(id, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserResponseDto>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return  ResponseEntity.ok(service.findAll(pageNumber, pageSize));
    }
}
