package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.configuration.JwtUtil;
import com.alkemy.wallet.dto.validator.IValidatorAuthenticationReq;
import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.AuthenticationResponse;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.utils.DTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;

    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
            DTOValidator.validate(authenticationRequest, IValidatorAuthenticationReq.class);
            String username = authenticationRequest.getUserName();
            String password = authenticationRequest.getPassword();
            if(username==null || password==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username, password) );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        if(userDetails==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(jwt));
    }
}
