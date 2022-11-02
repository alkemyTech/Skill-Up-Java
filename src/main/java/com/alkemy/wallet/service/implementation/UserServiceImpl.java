package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAllUsers() {
        var users = repository.findAll();
        return users.stream()
                    .map( mapper::convertToDto )
                    .toList();
    }

    @Override
    public UserDto createUser( UserRequestDto userRequestDto ) {
        return UserDto.builder()
                      .name( "Sample" )
                      .lastName( "User" )
                      .email( "user@sample.com" )
                      .build();
    }
}
