package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.RoleDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.exception.MessageErrorEnum;
import com.alkemy.wallet.model.entity.RoleEnum;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final BankDAO bankDAO;

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request) {

        Optional<UserEntity> userExists = Optional.ofNullable(bankDAO.findByEmail(request.getEmail()));

        if(userExists.isPresent()){
            throw new BankException(MessageErrorEnum.USER_EXISTS.getMessage());
        }

        if(!request.getRoleId().equalsIgnoreCase(RoleEnum.USER.getName()) && !request.getRoleId().equalsIgnoreCase(RoleEnum.ADMIN.getName())) {
            throw new BankException(MessageErrorEnum.INVALID_ROLE.getMessage());
        }

        int roleId = request.getRoleId().equalsIgnoreCase(RoleEnum.ADMIN.getName()) ? RoleEnum.ADMIN.getId() : RoleEnum.USER.getId();
        String roleName = request.getRoleId().equalsIgnoreCase(RoleEnum.ADMIN.getName()) ? RoleEnum.ADMIN.getName() : RoleEnum.USER.getName();
        RoleEnum roleEnum = request.getRoleId().equalsIgnoreCase(RoleEnum.ADMIN.getName()) ? RoleEnum.ADMIN : RoleEnum.USER;

        RoleDTO role = RoleDTO.builder()
                .id(roleId)
                .name(roleEnum)
                .description(roleName)
                .build();

        UserEntity userEntity = bankDAO.createUser(request, role);

        UserResponseDTO response = UserResponseDTO.builder()
                .user(userEntity.getEmail())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
