package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.UserRegisteredDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.security.AuthenticationRequest;
import com.alkemy.wallet.security.AuthenticationResponse;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.AuthenticationService;
import com.alkemy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<UserRegisteredDto> registerUser(UserRequestDto user ) {

        //Check One or more fields are empty
        if ( user.name() == null || user.lastName() == null || user.email() == null || user.password() == null )
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );

        var existingUser = ( User ) userService.loadUserByUsername( user.email() );
        //Check User already registered
        if ( existingUser != null ) {
            if ( existingUser.getSoftDelete() == Boolean.TRUE) {
                existingUser.setSoftDelete( Boolean.FALSE );
                //Add UserService update
                return ResponseEntity.status( HttpStatus.OK ).body( null );
            }
            else {
                return ResponseEntity.status( HttpStatus.CONFLICT ).body(null);
            }
        }

        User userSaved = userService.createUser(user);

        accountService.createAccountByUserId(userSaved.getUserId(), Currency.ARS);
        accountService.createAccountByUserId(userSaved.getUserId(),Currency.USD);

        String token = jwtUtil.generateToken(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.convertToRegisteredDto(userSaved,token));
    }

    public ResponseEntity<AuthenticationResponse> loginUser( AuthenticationRequest authenticationRequest ) {

        String username = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        //If one field is empty
        if ( username == null || password == null )
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( username, password ) );

        final UserDetails userDetails = userService.loadUserByUsername( authenticationRequest.getEmail() );

        if ( userDetails == null )
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( null );

        final String jwt = jwtUtil.generateToken( userDetails );

        return ResponseEntity.status( HttpStatus.OK ).body( new AuthenticationResponse( jwt ) );
    }


}
