package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(value = "page"  , required = false) Integer page){
        List<UserDTO> users;
        users =  page !=null ? userService.getUsersByPage(page) : userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUserById(@PathVariable Integer id){
       userService.deleteUserById(id);
       return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetail(@PathVariable Integer id){
        UserDTO userDTO = userService.getUserDatail(id);
        return ResponseEntity.ok().body(userDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetail(@PathVariable Integer id){
        UserDTO userDTO = userService.getUserDatail(id);
        return ResponseEntity.ok().body(userDTO);
    }
}
