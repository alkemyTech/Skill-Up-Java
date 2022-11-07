package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.UserDetailDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper mapper;

    public UserDto convertToDto( User user ) {
        return mapper.map( user, UserDto.class );
    }

    public User convertToEntity( UserDto userDto ) {
        return mapper.map( userDto, User.class );
    }

    public UserDetailDto convertToDetailDto(User user ) {
        return mapper.map( user, UserDetailDto.class );
    }
}

