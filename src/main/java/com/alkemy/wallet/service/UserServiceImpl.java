package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.user.AccountSinUserResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.model.mapper.response.UserListResponseDto;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.impl.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponseDto getUserById(Long id) {
        //TODO add logic to check if the ID is the same as the logged user (AuthService)
        Optional<User> dbResponse =  repository.findById(id);
        if (dbResponse.isEmpty())
            throw new EntityNotFoundException(String.format("User not found for id %s", id));
        return mapper.entity2Dto(dbResponse.get());
    }

    @Override
    public UserListResponseDto getUsers() {
        UserListResponseDto users = mapper.entityList2DtoList(repository.findAll());
        if (users.getUsers().isEmpty())
            throw new IllegalArgumentException(String.format("List is empty or null: %s", users.getUsers()));
        return users;
    }

    //TODO move this method to AccountService
    public AccountSinUserResponseDto getAccountUserById(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty())
            return null;
        return null;
    }
}
