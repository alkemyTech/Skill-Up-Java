package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Retrieves a list of users", description = "<h3>Lists registered users</h3>" +
            "<p>You can use the pagination param, which lists a maximum of 10 users per page." +
            "</br><b>Note: </b>By omitting the parameter, it will return the total list of users.</p>")
    @Parameters(value = { @Parameter(name = "page", description = "Number of page", example = "1", in = ParameterIn.QUERY)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Users", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode="401", description = "Unauthorizated")})
    //Mapping---------------------------------------
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO>users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }
    @GetMapping(path = "/pages")
    public ResponseEntity<UserPageDTO> getUsersByPage(@RequestParam(value = "page" ) Integer page){
        UserPageDTO userPageDTO =  userService.getUsersByPage(page);
        return ResponseEntity.ok().body(userPageDTO);
    }
    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Deletes a single user", description = "<h3>Deletes (soft delete) a single user by id</h3>")
    @Parameters(value = { @Parameter(name = "id", description = "User id to delete", example = "1", in = ParameterIn.QUERY)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode="401", description = "Unauthorizated")})
    //Mapping---------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUserById(@PathVariable Integer id){
       userService.deleteUserById(id);
       return ResponseEntity.ok().build();
    }
    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Retrieves a single user", description = "<h3>Return a single user by id/h3>")
    @Parameters(value = { @Parameter(name = "id", description = "User id", example = "1", in = ParameterIn.QUERY)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found User", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode="401", description = "Unauthorizated")})
    //Mapping---------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserDetail(@PathVariable Integer id){
        UserResponseDTO userDTO = userService.getUserDatail(id);
        return ResponseEntity.ok().body(userDTO);
    }
     //Documentation--------------------------------
     @Operation(security = {@SecurityRequirement(name = "Bearer")},
     summary = "Updates a single user", description = "<h3>Updates a single user by id/h3>")
    @Parameters(value = { @Parameter(name = "id", description = "User id", example = "1", in = ParameterIn.QUERY)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated user", content = {
         @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}),
        @ApiResponse(responseCode="401", description = "Unauthorizated")})
//Mapping---------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserUpdateDTO user,@PathVariable Integer id){
        UserDTO userDTO =  userService.updateUser(user,id);
        return ResponseEntity.ok().body(userDTO);
    }
}
