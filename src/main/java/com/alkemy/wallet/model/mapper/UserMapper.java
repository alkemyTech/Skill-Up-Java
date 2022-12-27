package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.request.UserUpdateRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final IAuthService authService;

    public User dto2Entity(UserRequestDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
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
                .role(entity.getRole().getName())
                .build();
    }

    public User refreshValues(UserUpdateRequestDto dto, User user) {
        if (dto.getFirstName() != null && !dto.getFirstName().trim().isEmpty())
            user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null && !dto.getLastName().trim().isEmpty())
            user.setLastName(dto.getLastName());
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty())
            user.setPassword(authService.encode(dto.getPassword()));
        return user;
    }
}