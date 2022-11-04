package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> showAllUsers() {
        return userRepository.findAll();
    }
}
