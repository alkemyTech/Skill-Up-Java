package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class UserMapper {

    public User dto2Entity(UserRequestDto dto, Set<Role> roles) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail().toLowerCase())
                .password(dto.getPassword())
                .creationDate(LocalDateTime.now())
                .roles(roles)
                .build();
    }

    public UserResponseDto entity2Dto(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .createdAt(entity.getCreationDate())
                .updatedAt(entity.getUpdateDate())
                .roles(entity.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    public User refreshValues(UserRequestDto dto, User entity2Return) {
        if (dto.getFirstName() != null && !dto.getFirstName().trim().isEmpty())
            entity2Return.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null && !dto.getLastName().trim().isEmpty())
            entity2Return.setLastName(dto.getLastName());
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty())
            entity2Return.setPassword(dto.getPassword());
        entity2Return.setUpdateDate(LocalDateTime.now());
        return entity2Return;
    }
}