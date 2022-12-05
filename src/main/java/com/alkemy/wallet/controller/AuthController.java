package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AuthToken;
import com.alkemy.wallet.dto.LoginUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.interfaces.ICustomUserDetailsService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@ApiModel("Controlador de Autorizaciones")
public class AuthController {
    @Autowired
    JwtUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ICustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> signUp(@Valid @RequestBody ResponseUserDto responseUserDto) {
        ResponseUserDto userSaved = customUserDetailsService.save(responseUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> signIn(@Valid @RequestBody LoginUserDto loginUser /*@RequestHeader("Authorization") String token*/) throws AuthenticationException {
        ResponseUserDto responseUser = customUserDetailsService.findByEmail(loginUser.getEmail());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        /*token =*/ jwtTokenUtil.create(authentication);
//        token = jwtTokenUtil.create(String.valueOf(customUserDetailsService.findByEmail(authentication.getName()).getId()),
//                customUserDetailsService.findByEmail(authentication.getName()).getEmail());
        return ResponseEntity.ok(new AuthToken(jwtTokenUtil.create(authentication)));
    }

//    @PostMapping("/register")
//    public String signUp(@Valid @RequestBody ResponseUserDto responseUserDto) {
//        return jwtTokenUtil.create(customUserDetailsService.save(responseUserDto).getId().toString(), responseUserDto.getPassword());
//    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/user")
    public ResponseEntity<ResponseUserDto> authenticatedUser() {
        return ResponseEntity.ok().body(customUserDetailsService.getUserAuthenticated());
    }
}
