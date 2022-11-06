package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.enumeration.RoleList;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.RoleRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;



@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
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
    public UserDTO createUser(UserDTO userDTO) {
        // Password encoding
        String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        // Adding role
        Role userRole = new Role();
        userRole.setName(RoleList.USER);
        userRole.setDescription("Created user with role USER");
        roleRepository.save(userRole);
        userDTO.setRole(userRole);
        // Persist entity
        User user = userMapper.userDTO2Entity(userDTO);
        userRepository.save(user);
        // Returning DTO response
        UserDTO userResponse = userMapper.createUserEntity2DTOResponse(user);
        return userResponse;
    }

    @Override
    public boolean deleteUserById(Integer id) {
       userRepository.deleteById(id);
       return true;
    }
}
