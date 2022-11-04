package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User>  users =  userRepository.findAll();
        List<UserDTO>  result  = userMapper.userEntityList2DTOList(users);
        return result;
    }
    /*@Override
    public List<UserDTO> getUsersForPage(Integer page) {
        Pageable pageWithTenElements = PageRequest.of(0, 10);

        Page<User> users =  userRepository.findAll(pageWithTenElements);
        List<User> userList = users.getContent();
        List<UserDTO>  result  = userMapper.userEntityList2DTOList(userList);
        return result;
    }
     */

    @Override
    public User createUser(UserDTO userDTO) {
        String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        User user = userMapper.userDTO2Entity(userDTO);
        userRepository.save(user);
        return user; 
    }
}
