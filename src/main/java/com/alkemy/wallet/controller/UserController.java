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
            summary = "User list", description = "<h3>Endpoint that lists the users that are registered in the database.</h3>" +
            "<p>You can use the pagination that lists maximum 10 users for page." +
            "</br><b>Note: </b>if You don't pass it the pagination parameter, it will list all the existing users in the database.</p>")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUserById(@PathVariable Integer id){
       userService.deleteUserById(id);
       return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserDetail(@PathVariable Integer id){
        UserResponseDTO userDTO = userService.getUserDatail(id);
        return ResponseEntity.ok().body(userDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserUpdateDTO user,@PathVariable Integer id){
        UserDTO userDTO =  userService.updateUser(user,id);
        return ResponseEntity.ok().body(userDTO);
    }
}
