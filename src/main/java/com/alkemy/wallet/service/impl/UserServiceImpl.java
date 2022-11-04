package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final BankDAO bankDAO;

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request) {
        //Falta hacer validacion de los campos con las anotaciones de los DTOs
        UserEntity userEntity = bankDAO.createUser(request);
        UserResponseDTO response = UserResponseDTO.builder()
                .user(userEntity.getEmail())
                .build();
       // throw new BankException("Error prueba");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
