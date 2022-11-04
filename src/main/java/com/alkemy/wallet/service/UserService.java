package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUsers();
   
    public User createUser(UserDTO userDTO);
}
