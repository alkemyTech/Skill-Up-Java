package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDetailDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.dto.UsersDTO;
import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.AuthenticationResponse;
import com.alkemy.wallet.model.UserEntity;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

    @GetMapping("/users/{id}")
    public UserDetailDTO getUserDetail(@PathVariable Long id) {
        return userService.getUserDetail(id);
    }

    @GetMapping("/users")
    public List<UsersDTO> showAllUsers() {
        return userService.showAllUsers();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest)  {
        return authenticationServiceImpl.login(authenticationRequest);
    }

    @PatchMapping("/users/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,@RequestBody UserRequestDTO user, AuthenticationRequest aut){
        return  userService.updateUserId(id, user, aut);
    }

    @GetMapping("/userspage")
    public ResponseEntity<Page<UserEntity>> showUsersPage(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size, Model model){
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        userService.addNavigationAttributesToModel(pageNumber,model,pageRequest);

        return userService.showUsersPage(pageRequest);
    }
}