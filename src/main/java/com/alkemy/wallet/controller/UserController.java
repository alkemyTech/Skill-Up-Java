package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.UserModelAssembler;
import com.alkemy.wallet.assembler.model.UserModel;
import com.alkemy.wallet.dto.AuthToken;
import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.interfaces.ICustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ICustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserModelAssembler userModelAssembler;
    @Autowired
    private PagedResourcesAssembler<ResponseUserDto> pagedResourcesAssembler;


    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update user",
            description = "Provide user logged details to update",
            tags = "Patch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully Logged",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Resource out of permissions",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<ResponseUserDto> updateUser(
            @Parameter(name = "First name, last name, email and password to update user",
                    required = true)
            @RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customUserDetailsService.update(requestUserDto));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    @Operation(summary = "User logged details",
            description = "Provides user logged details to verify",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "403", description = "Resource out of permissions",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<ResponseUserDto> getUserLoggedDetails(
            @Parameter(name = "Current logged user´s id",
                    required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok().body(customUserDetailsService.getUserLoggedById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Paged user list",
            description = "Provides a paged user list to be verified by administrators",
            tags = "Get",
            security = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "There is no user registered",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> getUserPage(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<ResponseUserDto> users = customUserDetailsService.findAllUsersPageable(page);
            PagedModel<UserModel> model = pagedResourcesAssembler.toModel(users, userModelAssembler);
            return ResponseEntity.ok().body(model);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list" + e.getMessage());
        }
    }

}
