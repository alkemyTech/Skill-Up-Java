package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AuthToken;
import com.alkemy.wallet.dto.LoginUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;
    private JwtUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> signUp(@Valid @RequestBody ResponseUserDto responseUserDto) {
        ResponseUserDto newUser = customUserDetailsService.save(responseUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthToken> signIn(@Valid @RequestBody LoginUserDto loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUserDetailsService.findByEmail(loginUser.getEmail()).getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.create(String.valueOf(customUserDetailsService.findByEmail(authentication.getName()).getId()),
                customUserDetailsService.findByEmail(authentication.getName()).getEmail());
        return ResponseEntity.ok(new AuthToken(token));

    }

}
