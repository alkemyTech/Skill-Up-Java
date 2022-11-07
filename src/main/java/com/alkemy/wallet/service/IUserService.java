package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
    public ResponseEntity<Object> deleteUser(Long userId);
    public List<UserEntity> showAllUsers();
}
