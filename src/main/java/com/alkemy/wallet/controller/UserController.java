package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDetailDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.dto.UserUpdateDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping( "/users" )
class UserController {
    private final UserService userservice;

    @GetMapping
    List<UserDto> getAll() {
        return userservice.getAllUsers();
    }

    @GetMapping( value = "/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<UserDetailDto> getUserDetailById(@PathVariable("id") Integer id, @RequestHeader("Authorization") String token ){
       User user = userservice.matchUserToToken(id,token);
       return ResponseEntity.ok(userservice.getUserDetailById(user.getUserId()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<UserUpdateDto> updateUser(@PathVariable("id") Integer id, @RequestBody UserUpdateDto userUpdateDto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userservice.updateUser(id,userUpdateDto,token));
    }


    // Capture token from headers
    // From service call JWT utils
    // Call loadUsersByUsername to get all User info
    @ResponseStatus( OK )
    @DeleteMapping( "/{id}" )
    void deleteById( @PathVariable Integer id, @RequestParam( "Authorization" ) String token ) throws ResourceNotFoundException {
        log.info( "User id: {}", id );
        log.info( "Token: {}", token );
        userservice.deleteUser( id );
    }
}
