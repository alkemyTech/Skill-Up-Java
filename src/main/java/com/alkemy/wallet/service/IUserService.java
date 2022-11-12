package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();

    UserRegisterDTO createUser(UserDTO userDTO);

    void deleteUserById(Integer id);

  //Declaracion de metodos para la seguridad
    String login(String email, String password);

    UserDetails loadUserByUsername(String email);

    UserPageDTO getUsersByPage(Integer page);

    UserDetailsDTO getUserDatail(Integer id);

    UserDTO updateUser(UserUpdateDTO userUpdateDTO, Integer id);
}
