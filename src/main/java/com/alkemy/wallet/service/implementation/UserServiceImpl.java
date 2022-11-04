package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAllUsers() {
        var users = repository.findAll();
        return users.stream()
                    .map( mapper::convertToDto )
                    .toList();
    }

    @Override
    public UserDto createUser(UserRequestDto userRequestDto) {

        User user = DtoToEntity(userRequestDto);

        // SET ROLE TO USER
        user.setRole(null);

        user.setCreationDate(new Timestamp(System.currentTimeMillis()));
        user.setSoftDelete(false);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncoded =passwordEncoder.encode(user.getPassword());
        user.setPassword(passEncoded);

        User aux;
        aux = repository.save(user);

        return entityToDTO(aux);
    }




    public UserDto entityToDTO(User user){
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(user,UserDto.class);
        return userDto;
    }

    public User DtoToEntity(UserRequestDto userDto){
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(userDto,User.class);
        return user;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }
}
