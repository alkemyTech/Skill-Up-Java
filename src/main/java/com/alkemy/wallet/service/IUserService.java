package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserService {
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
    public ResponseEntity<Object> deleteUser(Long userId);
    public List<UserEntity> showAllUsers();
    public Optional<UserEntity> findUserById(Long userId);
    public ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(Long userId);
}
