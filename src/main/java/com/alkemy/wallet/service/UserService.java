package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.UserNotFound;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    private final Mapper mapper;

    private final JwtUtil jwtUtil;

    public UserService(IUserRepository userRepository, Mapper mapper, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean checkLoggedUser(String token) {
        if (jwtUtil.getValue(token) != null) {
            return true;
        }
        throw new UserNotLoggedException("User not logged");
    }

    @Override
    public User findLoggedUser(String token) {
        User user = userRepository.findByEmail(jwtUtil.getValue(token));
        if (user != null) {
            return user;
        }
        throw new UserNotLoggedException("User not logged");
    }

    @Override
    public UserDto findByEmail(String email) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findOptionalByEmail(email);
        if (user.isPresent()) {
            return mapper.getMapper().map(user.get(), UserDto.class);
        }
        throw new ResourceNotFoundException("Email not found");
    }

    @Override
    public void softDelete(String token, Long id) {
        try {
            User user = findLoggedUser(token);

            User deleted = userRepository.findById(id).get();

            if (user.getRole().getName() == RoleName.ROLE_ADMIN) {

                deleted.setSoftDelete(Boolean.TRUE);

            } else if (user.getRole().getName() == RoleName.ROLE_USER && deleted == user) {

                user.setSoftDelete(Boolean.TRUE);

            }

        } catch (UserNotFound e) {

            throw new UserNotFound("user can't be delete");

        }

    }
}
