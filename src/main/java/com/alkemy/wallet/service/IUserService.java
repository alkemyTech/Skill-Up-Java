package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
import com.alkemy.wallet.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<UserDTO> getAllUsers();

    public UserRegisterDTO createUser(UserDTO userDTO);

    boolean deleteUserById(Integer id);

    public List<UserDTO> getUsersByPage(Integer page);

    public UserDTO getUserDatail(Integer id);

}
