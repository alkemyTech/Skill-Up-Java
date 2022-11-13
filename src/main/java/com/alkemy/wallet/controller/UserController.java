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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping( "/users" )
class UserController {
    private final UserService userservice;

    //Swagger Notation getAll
    @Operation(summary = "Get all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Users Info",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "401", description = "User Unauthorized",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Authorization information is missing or invalid")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot get Users data")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Users data weren't found")),}),
    })
    //end Swagger notation

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @GetMapping
    UserPaginatedDto getAll(@Param("page") Integer page, @Parameter(description = "authentication token") @RequestHeader( "Authorization" ) String token) {
        return userservice.getAllUsers(page, token);

    }

    //Swagger Notation getUserDetailById
    @Operation(summary = "Get User detail by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailDto.class))}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "401", description = "User Unauthorized",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Authorization information is missing or invalid")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot get Users data")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Users data weren't found")),}),
            @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Server couldn't get the User data")),})
    })
    //end Swagger notation
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping( value = "/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<UserDetailDto> getUserDetailById(@Parameter(description = "id user")@PathVariable("id") Integer id, @Parameter(description = "authentication token") @RequestHeader("Authorization") String token ){
       User user = userservice.matchUserToToken(id,token);
       return ResponseEntity.ok(userservice.getUserDetailById(user.getUserId()));
    }

    //Swagger Notation updateUser
    @Operation(summary = "Update User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User update successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateDto.class))}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "401", description = "User Unauthorized",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Authorization information is missing or invalid")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot get Users data")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Users data weren't found")),}),
            @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Server couldn't get the User data")),})
    })
    //end Swagger notation
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<UserUpdateDto> updateUser(@Parameter(description = "id user to be updated")@PathVariable("id") Integer id, @RequestBody UserUpdateDto userUpdateDto, @Parameter(description = "authentication token") @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userservice.updateUser(id,userUpdateDto,token));
    }

    //Swagger Notation deleteById
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Deletes User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(defaultValue = "Users data deleted")),}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "401", description = "User Unauthorized",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Authorization information is missing or invalid")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot delete User")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Users data weren't found")),}),
            @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Server couldn't delete User")),})
    })
    //end Swagger notation
    // Capture token from headers
    // From service call JWT utils
    // Call loadUsersByUsername to get all User info
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus( OK )
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping( "/{id}" )
    //TODO: A este metodo lo puede acceser Usuarios autenticados tambien, agregarle algo para negar eso y que sea solo para admin
    void deleteById( @Parameter(description = "id user to be deleted") @PathVariable Integer id, @Parameter(description = "authentication token") @RequestHeader( "Authorization" ) String token ) throws ResourceNotFoundException {
        userservice.deleteUser( id ,token);
    }
}
