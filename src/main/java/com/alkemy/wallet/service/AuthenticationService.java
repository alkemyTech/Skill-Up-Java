package com.alkemy.wallet.service;

import com.alkemy.wallet.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<User> registerUser(User user){

        //Check One or more fields are empty
        if(user.getFirstName()==null || user.getLastName()==null || user.getEmail()==null || user.getPassword()==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        //Check User already registered
        if(loadUserByUsername(user.getEmail())!=null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        //Crypt Password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }



}
