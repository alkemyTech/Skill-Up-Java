package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exception.ResourceFoundException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ICustomUserDetailsService;
import com.alkemy.wallet.service.interfaces.IRoleService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements ICustomUserDetailsService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseUserDto save(@Valid ResponseUserDto responseUserDto) throws ResourceFoundException {  /*Acordar exceptions*/

        if (userRepository.existsByEmail(responseUserDto.getEmail())) {
            throw new ResourceFoundException("User email already exists");
        }

        User user = mapper.getMapper().map(responseUserDto, User.class);
        user.setPassword(passwordEncoder.encode(responseUserDto.getPassword()));

        Role role = mapper.getMapper().map(roleService.findByName(RoleName.ROLE_USER), Role.class);
        user.setRole(role);
        user.setCreationDate(new java.util.Date());
        User userSaved = userRepository.save(user);

        this.authenticated(responseUserDto);

        accountService.createAccount(new Account(Currency.ars));
        accountService.createAccount(new Account(Currency.usd));

        return mapper.getMapper().map(userSaved, ResponseUserDto.class);

    }

    private String authenticated(ResponseUserDto responseUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(responseUserDto.getEmail(), responseUserDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.create(authentication);
    }

    @Override
    public ResponseUserDto update(@Valid ResponseUserDto responseUserDto) throws ResourceNotFoundException {  /*Acordar exceptions*/
        if (!userRepository.existsByEmail(responseUserDto.getEmail())) {
            throw new ResourceNotFoundException("User email does not exists");
        }

        userRepository.findByEmail(responseUserDto.getEmail()).setUpdateDate(Date.valueOf(LocalDate.now()));
        userRepository.findByEmail(responseUserDto.getEmail()).setPassword(passwordEncoder.encode(responseUserDto.getPassword()));
        userRepository.findByEmail(responseUserDto.getEmail()).setFirstName(responseUserDto.getFirstName());
        userRepository.findByEmail(responseUserDto.getEmail()).setLastName(responseUserDto.getLastName());

        return responseUserDto;
    }

    @Override
    public ResponseUserDto findByEmail(String email) throws ResourceNotFoundException {
        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User not found");
        }
        ResponseUserDto userDto = new ResponseUserDto();
        userDto.setId(userRepository.findByEmail(email).getId());
        userDto.setFirstName(userRepository.findByEmail(email).getFirstName());
        userDto.setLastName(userRepository.findByEmail(email).getLastName());
        userDto.setEmail(userRepository.findByEmail(email).getEmail());
        userDto.setPassword(userRepository.findByEmail(email).getPassword());

        return userDto;
    }

    @Override
    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public List<ResponseUserDto> findAll() {
        List<User> listaUser = userRepository.findAll();
        List<ResponseUserDto> listaResponse = new ArrayList<>();
        for (User user : listaUser) {
            ResponseUserDto responseUserDto = mapper.getMapper().map(user, ResponseUserDto.class);
            listaResponse.add(responseUserDto);
        }
        return listaResponse;
    }

    @Override
    public Page<ResponseUserDto> findAllPageable(Pageable pageable) {
        Page<User> listaUser = userRepository.findAll(pageable);
        return new PageImpl<ResponseUserDto>(
                findAll(), listaUser.getPageable(), listaUser.getTotalElements()
        );
    }

    @Override
    public ResponseUserDto getUserAuthenticated() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseUserDto userDto = mapper.getMapper().map(userRepository.findByEmail(email), ResponseUserDto.class);
        return userDto;
    }
}
