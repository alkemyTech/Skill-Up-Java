package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.user.AccountSinUserResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.response.user.IUserConAccountResponseMapper;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository repository;
    private final IUserConAccountResponseMapper mapper;

    public UserService(IUserRepository repository, IUserConAccountResponseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
}
