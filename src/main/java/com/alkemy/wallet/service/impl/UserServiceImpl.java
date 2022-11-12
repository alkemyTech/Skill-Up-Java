package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.UserCreateDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.enumeration.RoleList;
import com.alkemy.wallet.exception.RestServiceException;
import com.alkemy.wallet.exception.NotFoundException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.RoleRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.util.EmailValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    IAccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User>  users =  userRepository.findAll();
        return userMapper.userEntityList2DTOList(users);
    }

    @Override
    public List<UserDTO> getUsersByPage(Integer page) {
        Pageable pageWithTenElements = PageRequest.of(page - 1, 10);
        Page<User> users =  userRepository.findAll(pageWithTenElements);
        List<User> userList = users.getContent();
        return userMapper.userEntityList2DTOList(userList);
    }

    @Override
    public UserDTO getUserDatail(Integer id) {
        Optional<User> entity =userRepository.findById(id);
        if(entity.isEmpty()){
            throw new NotFoundException("Invalid ID");
        }
        return userMapper.userEntity2DTO(entity.get());
        //TODO: modify dto to return
    }

    @Override
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
		
		if (EmailValidator.emailRegexMatches(userCreateDTO.getEmail())) {
			try {
				EmailValidator.emailRegexMatches(userCreateDTO.getEmail());
				String encodedPassword = this.passwordEncoder.encode(userCreateDTO.getPassword());
				userCreateDTO.setPassword(encodedPassword);
				Role userRole = roleRepository.findByName(RoleList.USER);
				User user = userMapper.userCreateDTO2Entity(userCreateDTO, userRole);
				userRepository.save(user);        
				UserResponseDTO userResponseDTO = userMapper.userEntity2DTOResponse(user);
				accountService.createAccount(userResponseDTO.getId(), "ARS");
				accountService.createAccount(userResponseDTO.getId(), "USD");
				return userResponseDTO;
			} catch (Exception e) {
				throw new RestServiceException("user creation failed", HttpStatus.BAD_REQUEST);
			}
		} else {
			throw new RestServiceException("must be a valid email", HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }

    @Override
    public void deleteUserById(Integer id) {
        try {
            userRepository.deleteById(id);
        }
        catch (Exception err){
            throw new NotFoundException("Invalid ID");
        }
    }

	@Override
	@Transactional(readOnly = true)
	public String login(String email, String password) {
		try {
			// Validar datos de inicio de sesion
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			// Retorna token si los datos son correctos
			return jwtTokenProvider.createToken(userRepository.findByEmail(email).getEmail(), userRepository.findByEmail(email).getRole() );
		} catch (AuthenticationException e) {
			// Excepcion en caso de datos erroneos
			throw new RestServiceException("username o password invalido", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		// Se busca usuario por su username
				final User user = userRepository.findByEmail(email);
				// Se evalua si usuario existe
				if (user == null) {
					// Si no existe se retorna excepcion de "Usuario no encontrado"
					throw new UsernameNotFoundException("Usuario '" + email + "' no encontrado");
				}
				// Si existe, se retorna un objeto de tipo UserDetails, validando contraseña y
				// su respectivo Rol.
				return org.springframework.security.core.userdetails.User
						.withUsername(email)
						.password(user.getPassword())
						.authorities(user.getRole().getName())
						.accountExpired(false)
						.accountLocked(false)
						.credentialsExpired(false)
						.disabled(false)
						.build();
	}
}
