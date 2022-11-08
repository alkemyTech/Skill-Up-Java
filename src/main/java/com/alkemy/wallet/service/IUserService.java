package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;

import java.util.List;

public interface IUserService {
    public List<UserDTO> getAllUsers();

    public UserRegisterDTO createUser(UserDTO userDTO);

    public void deleteUserById(Integer id);

    public List<UserDTO> getUsersByPage(Integer page);

    public UserDTO getUserDatail(Integer id);


}
