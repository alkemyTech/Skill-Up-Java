package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.security.AuthenticationRequest;
import com.alkemy.wallet.security.AuthenticationResponse;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AuthenticationService;
import com.alkemy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public ResponseEntity<UserDto> registerUser(UserRequestDto user){

        //Check One or more fields are empty
        if(user.name()==null || user.lastName()==null || user.email()==null || user.password()==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        //Check User already registered
        if(userService.loadUserByUsername(user.email())!=null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
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
