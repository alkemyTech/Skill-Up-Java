package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.UserUpdateRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK).body(userService.getUserDetails(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id,
                                                      @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.status(OK).body(userService.updateUser(id, userUpdateRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(name = "page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(userService.getAllUsers(pageNumber));
    }
}
