package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.request.UserUpdateRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.User;
import org.springframework.data.domain.Page;

public interface IUserService {

    UserResponseDto saveNewUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto);

    void deleteUserById(Long id);

    UserResponseDto getUserDetails(Long id);

    void addAccountToUser(User user, Account account);

    User getUserById(Long id);

    User getUserByEmail(String email);

    boolean checkIfUserEmailExists(String email);

    Page<UserResponseDto> getAllUsers(Integer pageNumber);
}