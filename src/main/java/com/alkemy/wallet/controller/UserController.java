package com.alkemy.wallet.controller;

import com.alkemy.wallet.configuration.JwtUtil;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.impl.MyUserDetailsService;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.AuthenticationResponse;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final IUserService userService;
    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO user) {
        //final String jwt = jwtTokenUtil.generateToken(user.getEmail());
        return userService.createUser(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest)  {
        return authenticationServiceImpl.login(authenticationRequest);
    }
    @PatchMapping("/users/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,@RequestBody UserRequestDTO user, AuthenticationRequest aut){
        return  userService.updateUserId(id, user, aut);
    }

}
