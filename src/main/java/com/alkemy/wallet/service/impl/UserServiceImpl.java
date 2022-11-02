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

import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        Pageable pageWithTenElements = PageRequest.of(0, 10);

        Page<User> users =  userRepository.findAll(pageWithTenElements);
        List<User> userList = users.getContent();
        List<UserDTO>  result  = userMapper.userEntityList2DTOList(userList);
        return null;
    }
}
