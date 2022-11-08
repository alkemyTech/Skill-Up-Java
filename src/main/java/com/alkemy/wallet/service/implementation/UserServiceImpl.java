package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.UserRequestDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.RoleName;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        return users.stream().map( this::entityToDTO ).toList();
    }

    @Override
    public UserDto createUser( UserRequestDto userRequestDto ) {

        User user = DtoToEntity( userRequestDto );

        //SET ROLE TO USER
        //THIS ROLE CREATION MUST BE DELETED IN THE FUTURE
        RoleName roleName = RoleName.USER;
        Role role = new Role( roleName, "Rol de usuarios", new Timestamp( System.currentTimeMillis() ), null );
        user.setRole( role );

        user.setCreationDate( new Timestamp( System.currentTimeMillis() ) );
        user.setSoftDelete( false );

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncoded = passwordEncoder.encode( user.getPassword() );
        user.setPassword( passEncoded );

        return entityToDTO( repository.save( user ) );
    }

    @Override
    public void deleteUser( Integer id ) throws ResourceNotFoundException {
        try {
            repository.deleteById( id );
        } catch ( EmptyResultDataAccessException exception ) {
            throw new ResourceNotFoundException( exception.getMessage() );
        }
    }


    public UserDto entityToDTO( User user ) {
        //Mappers commented until they work automatically
//        ModelMapper mapper = new ModelMapper();
//        UserDto userDto = mapper.map(user,UserDto.class);
        return new UserDto( user.getFirstName(), user.getFirstName(), user.getEmail() );

    }

    public User DtoToEntity( UserRequestDto userDto ) {
        //Mappers commented until they work automatically
//        ModelMapper mapper = new ModelMapper();
//        User user = mapper.map(userDto,User.class);
        User user = new User();

        user.setPassword( userDto.password() );
        user.setEmail( userDto.email() );
        user.setFirstName( userDto.name() );
        user.setLastName( userDto.lastName() );

        return user;
    }

    @Override
    public User loadUserByUsername( String email ) throws UsernameNotFoundException {
        try {
            return repository.findByEmail( email );
        } catch ( UsernameNotFoundException exception ) {
            throw exception;
        }
    }
}
