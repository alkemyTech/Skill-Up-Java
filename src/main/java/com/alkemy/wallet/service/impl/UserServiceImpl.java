package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.UserRegisterDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements IUserService {

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
    public UserRegisterDTO createUser(UserDTO userDTO) {
        String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        Role userRole = roleRepository.findByName(RoleList.USER);
        userDTO.setRole(userRole);
        User user = userMapper.userDTO2Entity(userDTO);
        userRepository.save(user);        
        UserRegisterDTO userResponse = userMapper.createUserEntity2DTOResponse(user);
        accountService.createAccount(userResponse.getId(), "ARS");
        accountService.createAccount(userResponse.getId(), "USD");
        return userResponse;
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
	public String login(String email, String password) {
		try {
			// Validar datos de inicio de sesion
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			System.out.println(email);
			System.out.println(password);
			// Retorna token si los datos son correctos
			return jwtTokenProvider.createToken(email, userRepository.findByEmail(email).getRole());
		} catch (AuthenticationException e) {
			// Excepcion en caso de datos erroneos
			throw new RestServiceException("username o password invalido", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public String signUp(User user) {
		// Valida si el nombre de usuario no exista
				if (!userRepository.existsByEmail(user.getEmail())) {
				// Se encripta contraseña
					user.setPassword(passwordEncoder.encode(user.getPassword()));
				// Se almacena el usuario
				// Adding role
		        Role userRole = new Role();
		        userRole.setName(RoleList.USER);
		        userRole.setDescription("Created user with role USER");
		        roleRepository.save(userRole);
		        user.setRole(userRole);
				userRepository.save(user);
							// Retrona token valido para este usuario
				return jwtTokenProvider.createToken(user.getEmail(), user.getRole());
							
				} else {
							// En caso de que nombre de usuario exista se retonra excepcion
					throw new RestServiceException("Username ya esta en uso", HttpStatus.UNPROCESSABLE_ENTITY);
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
