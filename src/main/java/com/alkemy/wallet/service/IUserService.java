package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
import com.alkemy.wallet.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public interface IUserService {
    public List<UserDTO> getAllUsers();

    public UserRegisterDTO createUser(UserDTO userDTO);

    void deleteUserById(Integer id);
    
  //Declaracion de metodos para la seguridad
    public String login(String email, String password);
    
	public String signUp(User User);

    public UserDetails loadUserByUsername(String email);

    public List<UserDTO> getUsersByPage(Integer page);

    public UserDTO getUserDatail(Integer id);

}
