package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
    public ResponseEntity<Object> deleteUser(Long userId);
}
