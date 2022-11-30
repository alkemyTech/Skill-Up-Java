package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.security.service.UserDetailsCustomService;
import com.alkemy.wallet.security.jwt.JwtUtils;
import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.IRoleService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsCustomService userDetailsCustomService;
    private final JwtUtils jwtUtils;
    private final IUserService userService;
    private final IRoleService roleService;

    @Override
    public UserResponseDto register(UserRequestDto request) {
        if (userService.getByEmail(request.getEmail()) != null) {
            throw new EntityExistsException(
                    String.format("The email %s already exist in the data base", request.getEmail().toLowerCase()));
        }
        if (roleService.getAll().isEmpty())
            roleService.save();

        Role role = roleService.getById(request.getRoleId());
        return userService.save(request, role);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = generateToken(request.getEmail());
        return AuthResponseDto.builder()
                .email(request.getEmail())
                .token(token)
                .build();
    }

    @Override
    public String getEmailFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public String encode(String toEncode) {
        return new BCryptPasswordEncoder().encode(toEncode);
    }

    @Override
    public String generateToken(String userRequest) {
        return jwtUtils.generateToken(userDetailsCustomService.loadUserByUsername(userRequest));
    }
}
