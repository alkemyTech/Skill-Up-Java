package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exception.ResourceFoundException;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class CustomUserDetailsService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

/*
    Agregar en las configuraciones de seguridad
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }*/

//    @Autowired
//    private IRoleService roleService;
//
//    @Autowired
//    private IAccountService accountService;

    public ResponseUserDto save(@Valid ResponseUserDto responseUserDto) throws ResourceFoundException {  /*Acordar exceptions*/
        if (userRepository.existsByEmail(responseUserDto.getEmail())) {
            throw new ResourceFoundException("User email already exists");
        }

        User user = mapper.getMapper().map(responseUserDto, User.class);
        user.setPassword(passwordEncoder.encode(responseUserDto.getPassword()));

/*
 Agregar una vez que esten disponibles las entidades
*/
//        Role role = iRoleService.findByName(Role.ROLE_USER);
//        user.setRole(role); /*acomodar entidad Rol en User: un usuario tiene un rol*/

        User userSaved = userRepository.save(user);

//        accountService.addAccount(userSaved.getEmail(), Currency.USD));
//        accountService.addAccount(userSaved.getEmail(), Currency.ARS));

        ResponseUserDto userDto = mapper.getMapper().map(userSaved, ResponseUserDto.class);

        return userDto;

    }
}
