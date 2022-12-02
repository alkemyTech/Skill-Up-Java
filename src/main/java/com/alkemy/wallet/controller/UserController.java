package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUser(@RequestBody ResponseUserDto user) {
        if(!customUserDetailsService.existsById(user.getId())){ //el Id no existe? o no es correcto?
            System.out.println("Trying to update User without Id");
            return ResponseEntity.badRequest().build();
        }
        else{
            ResponseUserDto userUpDated = customUserDetailsService.update(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userUpDated);
        }
    }

}
