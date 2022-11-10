package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserRegisteredDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.security.AuthenticationRequest;
import com.alkemy.wallet.security.AuthenticationResponse;
import com.alkemy.wallet.service.implementation.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

    //Swagger Notation registerUser
    @Operation(summary = "Register a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creation successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRegisteredDto.class)) })
    })
    //End Swagger notation

    @PostMapping("/register")
    public ResponseEntity<UserRegisteredDto> registerUser(@RequestBody UserRequestDto user){
        return authenticationServiceImpl.registerUser(user);
    }

    //Swagger Notation loginUser
    @Operation(summary = "Performs user authentication via login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)) })
    })
    //End Swagger notation

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest){
        return authenticationServiceImpl.loginUser(authenticationRequest);
    }



}
