package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserDetailsDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
import com.alkemy.wallet.dto.UserUpdateDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();

    UserRegisterDTO createUser(UserDTO userDTO);

    void deleteUserById(Integer id);

  //Declaracion de metodos para la seguridad
    String login(String email, String password);

    UserDetails loadUserByUsername(String email);

    List<UserDTO> getUsersByPage(Integer page);

    UserDetailsDTO getUserDatail(Integer id);

    UserDTO updateUser(UserUpdateDTO userUpdateDTO, Integer id);
}
