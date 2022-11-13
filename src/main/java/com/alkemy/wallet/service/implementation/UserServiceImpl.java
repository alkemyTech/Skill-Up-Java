package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.ForbiddenAccessException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.RoleName;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.RoleRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final JWTUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserPaginatedDto getAllUsers(Integer page, String token) {
        String jwt;
        jwt = token.substring(7);
        String email = jwtUtil.extractUserName(jwt);
        User user = loadUserByUsername(email);
        if (user.getRole().getName().name().equals("USER")) {
            throw new ForbiddenAccessException("Can only access if you are an Admin");
        }else {

            Pageable pageable = PageRequest.of(page, 10);
            Page<User> userPage = userRepository.findAll(pageable);

            UserPaginatedDto userPaginatedDto = new UserPaginatedDto();

            List<UserDto> userDtoList = new ArrayList<UserDto>();

            for (User u : userPage) {
                userDtoList.add(userMapper.convertToDto(u));
            }

            userPaginatedDto.setUserList(userDtoList);

            String url = "http://localhost:8080/users?page=";

            if (userPage.hasPrevious())
                userPaginatedDto.setPreviousUrl(url + (page - 1));
            else
                userPaginatedDto.setPreviousUrl("");

            if (userPage.hasNext())
                userPaginatedDto.setNextUrl(url + (page + 1));
            else
                userPaginatedDto.setNextUrl("");

            return userPaginatedDto;
        }
    }

    @Override
    public User createUser( UserRequestDto userRequestDto ) {

        User user = userMapper.convertRequestDtoToEntity(userRequestDto);

        Role role;
        role=roleRepository.findById(1).orElse(new Role(1,RoleName.USER, "Users rol", new Timestamp( System.currentTimeMillis() ), null ));

        user.setRole(role);

        user.setCreationDate( new Timestamp( System.currentTimeMillis() ) );
        user.setSoftDelete( false );

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncoded = passwordEncoder.encode( user.getPassword() );
        user.setPassword( passEncoded );

        return userRepository.save(user);
    }

    @Override
    public void deleteUser( Integer id , String token) throws ResourceNotFoundException {
        try {
            User user = loadUserByUsername(jwtUtil.extractUserName(token.substring(7)));

            if(user.getRole().getName().name()=="ADMIN")
                userRepository.deleteById(id);
            else{
                if(user.getUserId()==id)
                    userRepository.deleteById(id);
                else
                    throw new ForbiddenAccessException("You cannot delete another user");
            }

        } catch ( EmptyResultDataAccessException exception ) {
            throw new ResourceNotFoundException( exception.getMessage() );
        }
    }

    @Override
    public UserDetailDto getUserDetailById(Integer Id) {
        var user = userRepository.findById(Id);
        if(user.isPresent()){
            return userMapper.convertToDetailDto(user.get());
        }else{
            throw new ResourceNotFoundException("User does not exist");
        }
    }

    @Override
    public User loadUserByUsername( String email ) throws UsernameNotFoundException {
        try {
            return userRepository.findByEmail( email );
        } catch ( UsernameNotFoundException exception ) {
            System.out.println("hola");
            throw exception;
        }
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.get().getRole().getName().name();

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("The user with id: " + id + " was not found");
        }

        return optionalUser.get();
    }



    public User matchUserToToken(Integer id, String token) throws ForbiddenAccessException {
        String jwt;
        jwt = token.substring(7);
        String email = jwtUtil.extractUserName(jwt);
        User user = loadUserByUsername(email);
        if(user.getUserId().equals(id)){
            return user;
        }else{
            throw new ForbiddenAccessException("Cannot access another user details");
        }
    }

    @Override
    public UserUpdateDto updateUser(Integer id, UserUpdateDto userUpdateDto, String token) throws ForbiddenAccessException {
        User user=this.getUserById(id);
        User userToken=loadUserByUsername(jwtUtil.extractClaimUsername(token.substring(7)));
        if(user.getUserId()!=userToken.getUserId()){

            throw new ForbiddenAccessException("You are trying to modify a user that is not you");
        }
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncoded =passwordEncoder.encode(userUpdateDto.getPassword());
        user.setPassword(passEncoded);
        Timestamp timestamp=new Timestamp(new Date().getTime());
        user.setUpdateDate(timestamp);
        userRepository.save(user);
        return userMapper.convertToUpdateDto(user);

    }


    @Override
    public void reactivateAccount(User user){
        userRepository.save(user);
    }
}