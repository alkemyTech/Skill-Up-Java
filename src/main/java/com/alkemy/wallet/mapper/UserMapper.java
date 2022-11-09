package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper mapper;

    public UserDto convertToDto( User user ) {
        //return mapper.map( user, UserDto.class );
        return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public User convertToEntity( UserDto userDto ) {
        return mapper.map( userDto, User.class );
    }

    public User convertRequestDtoToEntity(UserRequestDto userRequestDto){
        User user = new User();
        user.setFirstName(userRequestDto.name());
        user.setLastName(userRequestDto.lastName());
        user.setEmail(userRequestDto.email());
        user.setPassword(userRequestDto.password());

        return user;
    }

    public UserRegisteredDto convertToRegisteredDto(User user, String token){

        return new UserRegisteredDto(user.getFirstName(),user.getFirstName(), user.getEmail(),token);


    }

    public UserDetailDto convertToDetailDto(User user ) {
        return mapper.map( user, UserDetailDto.class );
    }

    public UserUpdateDto convertToUpdateDto(User user){return mapper.map( user, UserUpdateDto.class );}
}

