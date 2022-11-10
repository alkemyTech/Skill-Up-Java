package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
     ResponseEntity<Object> deleteUser(Long userId);
     ResponseEntity<Object> updateUserId(Long id, UserRequestDTO userRequestDTO, AuthenticationRequest aut);
}
