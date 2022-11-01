package com.alkemy.wallet.service;

import com.alkemy.wallet.model.User;
import com.alkemy.wallet.security.AuthenticationRequest;
import com.alkemy.wallet.security.AuthenticationResponse;
import com.alkemy.wallet.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

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

    public ResponseEntity <AuthenticationResponse> loginUser(AuthenticationRequest authenticationRequest){

        String username = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        //If one field is empty
        if(username==null || password==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());

        if(userDetails==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(jwt));
    }


}
