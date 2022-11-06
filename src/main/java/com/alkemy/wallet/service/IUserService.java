package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public interface IUserService {
    public List<UserDTO> getAllUsers();

    public UserDTO createUser(UserDTO userDTO);

    boolean deleteUserById(Integer id);

}
