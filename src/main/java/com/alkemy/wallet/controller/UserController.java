package com.alkemy.wallet.controller;


import com.alkemy.wallet.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.wallet.dto.ResponseUserDto;

import java.util.List;
@RequestMapping("api/v1/users")
@RestController
public class UserController {
    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUsers(@PathVariable Long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
    
    @Secured(value = { "ROLE_ADMIN" })
    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> findAllUsers () {
    	return ResponseEntity.ok(userService.findAllUsers());
    }
}
