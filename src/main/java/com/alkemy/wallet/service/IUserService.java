package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.AccountBasicDto;
import com.alkemy.wallet.dto.PageDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;


public interface IUserService {

  List<UserDto> listAllUsers();

  void update(UserDto user, Long id);

  UserDto findById(Long id);

  List<AccountBasicDto> getAccountsBalance();

  void delete(Long id);

  UserDto update(Long id, UserRequestDto updatedDto);


  PageDto<UserDto> findAllUsers(Pageable pageable, HttpServletRequest request);
}