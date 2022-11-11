package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.LoginDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDTO> createUser(@RequestBody UserDTO userDTO) {
        UserRegisterDTO userResponse = userService.createUser(userDTO);
        return ResponseEntity.ok().body(userResponse);
    }
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
		String token = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
		return ResponseEntity.ok().body(token);
	}

}
