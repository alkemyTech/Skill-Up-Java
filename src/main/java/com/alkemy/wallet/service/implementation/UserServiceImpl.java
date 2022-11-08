package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.ForbiddenAccessException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.RoleName;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
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
    private AccountServiceImpl accountService;

    @Override
    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map( userMapper::convertToDto )
                .toList();
    }

    @Override
    public UserDto createUser(UserRequestDto userRequestDto) {

        User user = DtoToEntity(userRequestDto);

        //SET ROLE TO USER
        //THIS ROLE CREATION MUST BE DELETED IN THE FUTURE
        RoleName roleName = RoleName.USER;
        Role role = new Role(roleName,"Rol de usuarios",new Timestamp(System.currentTimeMillis()),null);
        user.setRole(role);

        user.setCreationDate(new Timestamp(System.currentTimeMillis()));
        user.setSoftDelete(false);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passEncoded =passwordEncoder.encode(user.getPassword());
        user.setPassword(passEncoded);

        User userSaved = userRepository.save(user);

        UserDto userDto = entityToDTO(userSaved);

        accountService.createAccountByUserId(userSaved.getUserId(),Currency.ARS);
        accountService.createAccountByUserId(userSaved.getUserId(),Currency.USD);

        return userDto;
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

    public UserDto entityToDTO(User user){
        //Mappers commented until they work automatically
//        ModelMapper mapper = new ModelMapper();
//        UserDto userDto = mapper.map(user,UserDto.class);

        final String jwt = jwtUtil.generateToken(user);

        return new UserDto(user.getFirstName(),user.getFirstName(), user.getEmail(),jwt);

    }

    public User DtoToEntity(UserRequestDto userDto){
        //Mappers commented until they work automatically
//        ModelMapper mapper = new ModelMapper();
//        User user = mapper.map(userDto,User.class);
        User user = new User();

        user.setPassword(userDto.password());
        user.setEmail(userDto.email());
        user.setFirstName(userDto.name());
        user.setLastName(userDto.lastName());

        return user;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);

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
    public UserUpdateDto updateUser(Integer id, UserUpdateDto userUpdateDto, String token) {
        User user=this.getUserById(id);
        User userToken=loadUserByUsername(jwtUtil.extractClaimUsername(token.substring(7)));
        if(user.getUserId()!=userToken.getUserId()){

            throw new ResourceNotFoundException("you are trying to modify a user that is not you");
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
}