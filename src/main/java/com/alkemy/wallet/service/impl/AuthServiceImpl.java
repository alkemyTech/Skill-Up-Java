package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.auth.service.UserDetailsCustomService;
import com.alkemy.wallet.auth.utility.JwtUtils;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.RoleEnum;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsCustomService userDetailsCustomService;
    private final JwtUtils jwtUtils;
    private final IUserRepository repository;
    private final UserMapper mapper;
    private final IRoleRepository roleRepository;
    private final IAccountService accountService;

    @Override
    public UserResponseDto register(UserRequestDto request) {
        if (getByEmail(request.getEmail()) != null) {
            log.error("User with email {} not found in the database", request.getEmail());
            throw new EntityExistsException(String.format("The email %s already exist in the data base", request.getEmail()));
        }
        log.info("User {} found in the database", request.getEmail());
        Set<Role> roles = new HashSet<>();
        Role role = saveRole(request.getRoleId());
        if (role == null) {
            role = getRoleById(request.getRoleId());
        }
        roles.add(role);
        User entity = mapper.dto2Entity(request, roles);
        entity.setPassword(encode(entity.getPassword()));
        entity.setSoftDelete(false);

        List<Account> accountList = accountService.createUserAccounts(entity);
        entity.setAccounts(accountList);

    return mapper.entity2Dto(repository.save(entity));
    }

    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        List<User> deletedUsers = repository.findAllDeleted();
        deletedUsers.forEach(deletedUser -> {
            if (request.getEmail().equalsIgnoreCase(deletedUser.getEmail()))
                throw new AccessDeniedException("User disabled");
        });
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = generateToken(request.getEmail());
        return AuthResponseDto.builder()
                .email(request.getEmail())
                .token(token)
                .build();
    }

    @Override
    public User getByEmail(String email) {
        Optional<User> response = repository.findByEmail(email);
        return response.orElse(null);
    }

    @Override
    public User getUserFromToken(String token) {
        if (!token.contains("Bearer "))
            throw new MalformedJwtException("Invalid token");
        String cleanToken = token.substring(7);
        String email = jwtUtils.extractUsername(cleanToken);
        if (!jwtUtils.validateToken(cleanToken, userDetailsCustomService.loadUserByUsername(email))) {
            throw new AccessDeniedException("Invalid token, not from logged user. JWT validity cannot be asserted and should not be trusted");
        }
        return getByEmail(email);
    }

    @Override
    public Role getRoleById(Long roleId) {
        Optional<Role> response =  roleRepository.findById(roleId);
        if (response.isEmpty()) {
            log.error("Role with id {} not found in the database", roleId);
            throw new NullPointerException(String.format("Invalid role id: %s. Try USER(1) or ADMIN(2)", roleId));
        }
        log.info("{} with id {} found in the database", response.get().getName(), response.get().getId());
        return response.get();
    }

    @Override
    public String encode(String toEncode) {
        return bCryptPasswordEncoder.encode(toEncode);
    }

    protected String generateToken(String userRequest) {
        return jwtUtils.generateToken(userDetailsCustomService.loadUserByUsername(userRequest));
    }

    protected Role saveRole(Long id) {
        if (roleRepository.findById(id).isPresent()) {
            return getRoleById(id);
        }
        log.info("Role with id {} not found", id);
        roleRepository.save(new Role(1L, RoleEnum.USER.getFullRoleName(), RoleEnum.USER.getSimpleRoleName(), LocalDateTime.now(), null));
        roleRepository.save(new Role(2L, RoleEnum.ADMIN.getFullRoleName(), RoleEnum.ADMIN.getSimpleRoleName(), LocalDateTime.now(), null));
        return null;
    }
}
