package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.ForbiddenAccessException;
import com.alkemy.wallet.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserPaginatedDto getAllUsers(Integer page, String token);
    User createUser(UserRequestDto userRequestDto);
    UserDetailDto getUserDetailById(Integer Id);
    User matchUserToToken(Integer id, String token) throws ForbiddenAccessException;
    User loadUserByUsername(String email);
    User getUserById(Integer id);

    UserUpdateDto updateUser(Integer id, UserUpdateDto userUpdateDto, String token);

    void deleteUser( Integer id, String token) throws ResourceNotFoundException;

    void reactivateAccount(User user);
}
