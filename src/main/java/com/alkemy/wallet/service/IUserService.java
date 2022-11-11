package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserDetailDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.UserEntity;
import com.alkemy.wallet.model.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
     ResponseEntity<Object> updateUserId(Long id, UserRequestDTO userRequestDTO, AuthenticationRequest aut);
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
    public ResponseEntity<Object> deleteUser(Long userId);
    public List<UserEntity> showAllUsers();
    public ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(Long userId);

   UserDetailDTO getUserDetail(Long id);
}
