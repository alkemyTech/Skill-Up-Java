package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    private final Mapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    public UserService(IUserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean checkLoggedUser(String token) {
        if (jwtUtil.getValue(token) != null)
            return true;
        else throw new UserNotLoggedException("User not logged");
    }

    @Override
    public UserDto findByEmail(String email) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findOptionalByEmail(email);
        if (user.isPresent()) {
            return mapper.getMapper().map(user.get(), UserDto.class);
        }
        throw new ResourceNotFoundException("Email not found");
    }

}
