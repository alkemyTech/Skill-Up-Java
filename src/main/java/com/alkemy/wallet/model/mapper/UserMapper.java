package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.request.UserRequestDto;
import com.alkemy.wallet.model.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.response.list.UserListResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User dto2Entity(UserRequestDto dto, Set<Role> roles) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .creationDate(LocalDateTime.now())
                .roles(roles)
                .build();
    }

    public UserResponseDto entity2Dto(User entity) {
        if (entity == null)
            return null;
        return UserResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .createdAt(entity.getCreationDate())
                .updatedAt(entity.getUpdateDate())
                .roles(
                        entity.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                )
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

    public UserListResponseDto entityList2DtoList(List<User> entityList) {
        return UserListResponseDto.builder()
                .users(entityList.stream().map(this::entity2Dto).collect(Collectors.toList()))
                .build();
    }
}
