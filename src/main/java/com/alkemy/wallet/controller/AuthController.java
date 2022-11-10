package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    
	/*
	 * @PostMapping("/login") 
	 * public String login(@RequestParam String email, @RequestParam String password) { 
	 * System.out.println(email);
	 * System.out.println(password); 
	 * return userService.login(email, password); }
	 */
    
    @PostMapping("/signup") //permite registrar usuario y que entregue un token de vuelta
 	@ResponseStatus(code = HttpStatus.CREATED)
 	public String signup(@RequestBody User User) {
 		System.out.println(User.toString());
 		return userService.signUp(User);
 	}
}
