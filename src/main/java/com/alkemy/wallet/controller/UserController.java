package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final IUserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/users")
    public List<UserEntity> showAllUsers() {
        return userService.showAllUsers();
    }

    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(@PathVariable Long userId) {
        return userService.showAllAccountsByUserId(userId);
    }
}