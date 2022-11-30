package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK).body(service.getDetails(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateById(@PathVariable("id") Long id,
                                                      @RequestBody UserRequestDto request) {
        return ResponseEntity.status(OK).body(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAll(@RequestParam(name = "page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(service.findAll(pageNumber));
    }
}
