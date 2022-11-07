package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping( "/users" )
class UserController {
    private final UserService service;

    @GetMapping
    List<UserDto> getAll() {
        return service.getAllUsers();
    }

    // Capture token from headers
    // From service call JWT utils
    // Call loadUsersByUsername to get all User info
    @ResponseStatus( OK )
    @DeleteMapping( "/{id}" )
    void deleteById( @PathVariable Integer id, @RequestParam( "Authorization" ) String token ) throws ResourceNotFoundException {
        log.info( "User id: {}", id );
        log.info( "Token: {}", token );
        service.deleteUser( id );
    }
}
