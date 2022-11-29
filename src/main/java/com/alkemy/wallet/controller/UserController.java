package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable("id") Long id) {
        UserResponseDto response = service.getDetails(id);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateById(@PathVariable("id") Long id, @RequestBody UserRequestDto request) {
        UserResponseDto response = service.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Page<UserResponseDto>> findAll(@RequestParam(name = "page") Integer pageNumber) {
        return  ResponseEntity.ok(service.findAll(pageNumber));
    }
}
