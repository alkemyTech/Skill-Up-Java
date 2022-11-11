package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.model.AuthenticationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface IUserService {
    ResponseEntity<Object> updateUserId(Long id, UserRequestDTO userRequestDTO, AuthenticationRequest aut);
    ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request);
    ResponseEntity<Object> deleteUser(Long userId);
    List<UserEntity> showAllUsers();
    ResponseEntity<List<AccountEntity>> showAllAccountsByUserId(Long userId);
    ResponseEntity<Page<UserEntity>> showUsersPage(PageRequest pageRequest);
    void addNavigationAttributesToModel(int pageNumber, Model model, PageRequest pageRequest);
}
