package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface IUserService {

    UserResponseDto save(UserRequestDto request, Role role);

    UserResponseDto update(Long id, UserRequestDto request);

    User getByEmail(String email);

    void addAccount(User user, Account account);

    User getById(Long id);

    UserResponseDto getUserDetails(Long id);

    void deleteUserById(Long id);

    Page<UserResponseDto> findAll(Integer pageNumber);
}