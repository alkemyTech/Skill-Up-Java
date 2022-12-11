package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AuthToken;
import com.alkemy.wallet.dto.LoginUserDto;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.interfaces.ICustomUserDetailsService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class AuthController {
    @Autowired
    JwtUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ICustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Provide user's details to register him",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "409", description = "Failed to register due to 'Email already in use'", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Failed to register due to 'Not-nullable field null or empty'",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RequestUserDto.class))})
    })
    public ResponseEntity<ResponseUserDto> signUp(
            @Parameter(description = "Email, first name, last name and password to register user",
                    required = true)
            @Valid @RequestBody RequestUserDto requestUserDto) {
        ResponseUserDto userSaved = customUserDetailsService.save(requestUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in",
            description = "Provide user's email and password to log in",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Logged",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthToken.class))}),
            @ApiResponse(responseCode = "403", description = "Bad login attempt",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<AuthToken> signIn(
            @Parameter(name = "Email and password to sign in",
                    required = true)
            @Valid @RequestBody LoginUserDto loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new AuthToken(jwtTokenUtil.create(authentication)));
    }

    @GetMapping("/logout")
    @Operation(summary = "Log out",
            description = "Ends user session with security context",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Logged out",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "No active session",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
