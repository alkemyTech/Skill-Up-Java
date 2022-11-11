package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.model.AuthenticationRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
     ResponseEntity<Object> updateUserId(Long id, UserRequestDTO userRequestDTO, AuthenticationRequest aut);
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
    public ResponseEntity<Object> deleteUser(Long userId);
    public List<UserEntity> showAllUsers();
    public ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(Long userId);
    public ResponseEntity<Page<UserEntity>> showUsersPage(int pageNumber);
}
