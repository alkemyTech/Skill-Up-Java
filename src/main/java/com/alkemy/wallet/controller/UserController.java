package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users =  userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }
}
