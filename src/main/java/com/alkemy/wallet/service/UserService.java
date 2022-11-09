package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserDetailDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.dto.UserUpdateDto;
import com.alkemy.wallet.exception.ForbiddenAccessException;
import com.alkemy.wallet.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDto> getAllUsers();
    User createUser(UserRequestDto userRequestDto);
    UserDetailDto getUserDetailById(Integer Id);
    User matchUserToToken(Integer id, String token) throws ForbiddenAccessException;
    User loadUserByUsername(String email);
    User getUserById(Integer id);

    UserUpdateDto updateUser(Integer id, UserUpdateDto userUpdateDto, String token);

    void deleteUser( Integer id ) throws ResourceNotFoundException;
}
