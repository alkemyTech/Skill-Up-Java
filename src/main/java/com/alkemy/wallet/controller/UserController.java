package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.AuthenticationResponse;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.DefaultPageable;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final IUserService userService;

    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

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

    @GetMapping("/userspage/{pageNumber}")
    public ResponseEntity<Page<UserEntity>> showUsersPage(@PathVariable int pageNumber){
        return userService.showUsersPage(pageNumber);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest)  {
        return authenticationServiceImpl.login(authenticationRequest);
    }
    @PatchMapping("/users/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,@RequestBody UserRequestDTO user, AuthenticationRequest aut){
        return  userService.updateUserId(id, user, aut);
    }
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(@PathVariable Long userId) {
        return userService.showAllAccountsByUserId(userId);
    }
}