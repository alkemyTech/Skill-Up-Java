package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.RequestUserDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exception.ResourceFoundException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ICustomUserDetailsService;
import com.alkemy.wallet.service.interfaces.IRoleService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CustomUserDetailsService implements ICustomUserDetailsService {

    @Autowired
    JwtUtil jwtTokenUtil;
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
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseUserDto save(@Valid RequestUserDto requestUserDto) throws ResourceFoundException {  /*Acordar exceptions*/

        if (userRepository.existsByEmail(requestUserDto.getEmail())) {
            throw new ResourceFoundException("User email already exists");
        }

        User user = mapper.getMapper().map(requestUserDto, User.class);
        user.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));

        Role role = mapper.getMapper().map(roleService.findByName(RoleName.ROLE_USER), Role.class);
        user.setRole(role);
        user.setCreationDate(new Date());
        User userSaved = userRepository.save(user);

        String token = this.authenticated(requestUserDto);

        accountService.createAccount(new Account(Currency.ars));
        accountService.createAccount(new Account(Currency.usd));

        ResponseUserDto responseUserDto = mapper.getMapper().map(userSaved, ResponseUserDto.class);
        responseUserDto.setToken(token);

        return responseUserDto;
    }

    @Override
    public ResponseUserDto saveAdmin(@Valid RequestUserDto requestUserDto) throws ResourceFoundException {  /*Acordar exceptions*/

        if (userRepository.existsByEmail(requestUserDto.getEmail())) {
            throw new ResourceFoundException("User email already exists");
        }

        User user = mapper.getMapper().map(requestUserDto, User.class);
        user.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));

        Role role = mapper.getMapper().map(roleService.findByName(RoleName.ROLE_ADMIN), Role.class);
        user.setRole(role);
        user.setCreationDate(new Date());
        User userSaved = userRepository.save(user);

        String token = this.authenticated(requestUserDto);

        accountService.createAccount(new Account(Currency.ars));
        accountService.createAccount(new Account(Currency.usd));

        ResponseUserDto responseUserDto = mapper.getMapper().map(userSaved, ResponseUserDto.class);
        responseUserDto.setToken(token);

        return responseUserDto;

    }

    private String authenticated(RequestUserDto requestUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUserDto.getEmail(), requestUserDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.create(authentication);
        return token;
    }

    @Override
    public ResponseUserDto update(@Valid RequestUserDto requestUserDto) throws ResourceNotFoundException {  /*Acordar exceptions*/
        if (!userRepository.existsByEmail(requestUserDto.getEmail())) {
            throw new ResourceNotFoundException("User email does not exists");
        }
        User user = userRepository.findByEmail(requestUserDto.getEmail());

        user.setUpdateDate(new Date());
        user.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));
        user.setFirstName(requestUserDto.getFirstName());
        user.setLastName(requestUserDto.getLastName());

        User userUpdated = userRepository.save(user);

        return mapper.getMapper().map(userUpdated, ResponseUserDto.class);
    }

    @Override
    public ResponseUserDto findByEmail(String email) throws ResourceNotFoundException {
        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findByEmail(email);

        return mapper.getMapper().map(user, ResponseUserDto.class);

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
    @Transactional
    public Page<ResponseUserDto> findAllUsersPageable(int page) throws Exception {
        try {

            Pageable pageable = PageRequest.of(page, 10);
            Page<ResponseUserDto> userPage = userRepository.findAll(pageable).map((user) -> mapper.getMapper().map(user, ResponseUserDto.class));

            return userPage;

        } catch (Exception e){

            throw new Exception(e.getMessage());

        }
    }

    @Override
    public ResponseUserDto getUserAuthenticated() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseUserDto userDto = mapper.getMapper().map(userRepository.findByEmail(email), ResponseUserDto.class);
        return userDto;
    }

    @Override
    public ResponseUserDto getUserLoggedById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(Objects.equals(userRepository.findByEmail(auth.getName()).getId(), id))) {
            throw new ResourceFoundException("Resource out of permissions");
        }
        User user = userRepository.findByEmail(auth.getName());

        return mapper.getMapper().map(user, ResponseUserDto.class);
    }


}
