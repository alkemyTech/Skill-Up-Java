package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    //Swagger Notation getTransactionDetailById
    @Operation(summary = "Get all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Users Info",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) })
    })
    //end Swagger notation

    @GetMapping
    List<UserDto> getAll() {
        return userservice.getAllUsers();
    }

    //Swagger Notation getTransactionDetailById
    @Operation(summary = "Get User detail by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailDto.class)) })
    })
    //end Swagger notation

    @GetMapping( value = "/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<UserDetailDto> getUserDetailById(@Parameter(description = "id user")@PathVariable("id") Integer id, @Parameter(description = "authentication token") @RequestHeader("Authorization") String token ){
       User user = userservice.matchUserToToken(id,token);
       return ResponseEntity.ok(userservice.getUserDetailById(user.getUserId()));
    }

    //Swagger Notation getTransactionDetailById
    @Operation(summary = "Update User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User update successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateDto.class)) })
    })
    //end Swagger notation

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<UserUpdateDto> updateUser(@Parameter(description = "id user to be updated")@PathVariable("id") Integer id, @RequestBody UserUpdateDto userUpdateDto, @Parameter(description = "authentication token") @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userservice.updateUser(id,userUpdateDto,token));
    }

    //Swagger Notation getTransactionDetailById
    @Operation(summary = "Deletes User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successful",
                    content = { @Content(mediaType = "application/json") })
    })
    //end Swagger notation
    // Capture token from headers
    // From service call JWT utils
    // Call loadUsersByUsername to get all User info
    @ResponseStatus( OK )
    @DeleteMapping( "/{id}" )
    void deleteById( @Parameter(description = "id user to be deleted") @PathVariable Integer id, @Parameter(description = "authentication token") @RequestParam( "Authorization" ) String token ) throws ResourceNotFoundException {
        log.info( "User id: {}", id );
        log.info( "Token: {}", token );
        userservice.deleteUser( id );
    }
}
