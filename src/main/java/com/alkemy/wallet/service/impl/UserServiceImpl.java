package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;

    private final UserMapper mapper;

    private final IAuthService authService;

    @Override
    public UserResponseDto getUserById(Long id, String token) {
        User userFromToken = authService.getUserFromToken(token);
        Optional<User> dbResponse = repository.findById(id);
        if (dbResponse.isEmpty())
            throw new EntityNotFoundException(String.format("User not found for id %s", id));
        if (!userFromToken.equals(dbResponse.get()))
            throw new AccessDeniedException("Access denied");
        return mapper.entity2Dto(dbResponse.get());
    }

    @Override
    public UserListResponseDto getUsers() {
        UserListResponseDto users = mapper.entityList2DtoList(repository.findAll());
        if (users.getUsers().isEmpty())
            throw new IllegalArgumentException(String.format("List is empty or null: %s", users.getUsers()));
        return users;
    }

    @Override
    public UserResponseDto deleteUserById(Long userId) {
        if (repository.existsById(userId)) {
            User deletedUser = repository.findById(userId).get();
            deletedUser.setUpdateDate(LocalDateTime.now());
            repository.save(deletedUser);
            deletedUser.setSoftDelete(true);

            repository.deleteById(userId);
            return mapper.entity2Dto(deletedUser);
        } else {
            throw new EntityNotFoundException(String.format("User with id: %s was not found or was already deleted", userId.toString()));
        }

    }
}
