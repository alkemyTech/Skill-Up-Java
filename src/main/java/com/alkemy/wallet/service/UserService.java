package com.alkemy.wallet.service;

import com.alkemy.wallet.controller.exception.ExceptionHandler;
import com.alkemy.wallet.model.dto.response.user.AccountSinUserResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.response.user.IUserConAccountResponseMapper;
import com.alkemy.wallet.model.mapper.response.user.IUserResponseMapper;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository repository;
    private final IUserConAccountResponseMapper mapper;
    private final IUserResponseMapper userResponseMapper;

    public UserService(IUserRepository repository, IUserConAccountResponseMapper mapper, IUserResponseMapper userResponseMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.userResponseMapper=userResponseMapper;
    }

    public List<UserResponseDto> getUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty())
            return Collections.emptyList();
        return mapper.listUserToListUserResponse(users);
    }

    public AccountSinUserResponseDto getAccountUserById(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty())
            return null;
        return mapper.userToAccountSinUserResponse(user.get());
    }

    public UserResponseDto deleteUserById(Long userId) {
        if(repository.existsById(userId)){
            User deletedUser =  repository.findById(userId).get();
            deletedUser.setUpdateDate(LocalDateTime.now());
            repository.save(deletedUser);
            deletedUser.setSoftDelete(true);

            repository.deleteById(userId);
            return userResponseMapper.userToUserResponse(deletedUser);
        }else {
            throw new EntityNotFoundException(String.format("User with id: %s was not found or was already deleted",userId.toString()));
        }
    }
}
