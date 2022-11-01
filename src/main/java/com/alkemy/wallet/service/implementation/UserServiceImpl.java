package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    @Override
    public UserDto createUser( UserRequestDto userRequestDto ) {
        return UserDto.builder()
                      .name( "Sample" )
                      .lastName( "User" )
                      .email( "user@sample.com" )
                      .password( "1234" )
                      .build();
    }
}
