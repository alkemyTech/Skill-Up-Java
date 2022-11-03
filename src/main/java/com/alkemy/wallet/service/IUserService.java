package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();

    boolean deleteUserById(Integer id);

}
