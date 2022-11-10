package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
import com.alkemy.wallet.enumeration.RoleList;
import com.alkemy.wallet.exception.NotFoundException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.RoleRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    IAccountService accountService;
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

    @Override
    public List<UserDTO> getUsersByPage(Integer page) {
        Pageable pageWithTenElements = PageRequest.of(page - 1, 10);
        Page<User> users =  userRepository.findAll(pageWithTenElements);

        List<User> userList = users.getContent();
        List<UserDTO>  result  = userMapper.userEntityList2DTOList(userList);
        return result;
    }

    @Override
    public UserDTO getUserDatail(Integer id) {
        Optional<User> entity =userRepository.findById(id);
        if(entity.isEmpty()){
            throw new NotFoundException("Invalid ID");
        }
        return userMapper.userEntity2DTO(entity.get());
        //TODO: modify dto to return
    }

    @Override
    public UserRegisterDTO createUser(UserDTO userDTO) {
        String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        Role userRole = roleRepository.findByName(RoleList.USER);
        userDTO.setRole(userRole);
        User user = userMapper.userDTO2Entity(userDTO);
        userRepository.save(user);        
        UserRegisterDTO userResponse = userMapper.createUserEntity2DTOResponse(user);
        accountService.createAccount(userResponse.getId(), "ARS");
        accountService.createAccount(userResponse.getId(), "USD");
        return userResponse;
    }

    @Override
    public boolean deleteUserById(Integer id) {
        try {
            userRepository.deleteById(id);
            return true;
        }
        catch (Exception err){
            return false;
        }
    }
}
