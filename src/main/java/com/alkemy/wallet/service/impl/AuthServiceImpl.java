package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.security.jwt.JwtUtils;
import com.alkemy.wallet.security.service.UserDetailsCustomService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.utils.CustomMessageSource;
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
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsCustomService userDetailsCustomService;
    private final JwtUtils jwtUtils;
    private final IUserService userService;
    private final CustomMessageSource messageSource;

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {
        boolean emailExist = userService.checkIfUserEmailExists(userRequestDto.getEmail().toLowerCase());
        if (emailExist) {
            throw new EntityExistsException(messageSource
                    .message("user.duplicated-email", new String[]{userRequestDto.getEmail().toLowerCase()}));
        }
        return userService.saveNewUser(userRequestDto);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        String token = generateToken(authRequestDto.getEmail());
        return AuthResponseDto.builder()
                .email(authRequestDto.getEmail())
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
    public String generateToken(String email) {
        return jwtUtils.generateToken(userDetailsCustomService.loadUserByUsername(email));
    }
}
