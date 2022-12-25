package com.alkemy.wallet.service;

import com.alkemy.wallet.model.constant.AccountCurrencyEnum;
import com.alkemy.wallet.model.constant.RoleEnum;
import com.alkemy.wallet.model.dto.request.AuthRequestDto;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.AuthResponseDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.jwt.JwtUtils;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import com.alkemy.wallet.utils.CustomMessageSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    protected IUserRepository userRepository;
    @Mock
    protected UserMapper userMapper;
    @Mock
    protected IAccountService accountService;
    @Mock
    protected IAuthenticationService authService;
    @Mock
    protected IRoleService roleService;
    @Mock
    protected CustomMessageSource messageSource;
    @Mock protected JwtUtils jwtUtils;

    @InjectMocks
    protected UserServiceImpl underTest;

    protected AuthRequestDto authRequestDto;
    protected AuthResponseDto authResponseDto;
    protected UserRequestDto userRequestDto1;
    protected UserRequestDto userRequestDto2;
    protected User user1;
    protected User user2;
    protected Role ROLE_ADMIN;
    protected Role ROLE_USER;
    protected UserResponseDto userResponseDto1;
    protected UserResponseDto userResponseDto2;
    protected Set<Role> rolesUser1;
    protected Set<Role> rolesUser2;
    protected List<Account> accountsUser1;
    protected List<Account> accountsUser2;

    @BeforeEach
    void setUp() {
        // Instantiating roles
        ROLE_ADMIN = new Role();
        ROLE_ADMIN.setName(RoleEnum.ADMIN.getFullRoleName());
        ROLE_ADMIN.setDescription(RoleEnum.ADMIN.getSimpleRoleName());
        ROLE_ADMIN.setCreationDate(LocalDateTime.now());
        ROLE_ADMIN.setUpdateDate(null);

        ROLE_USER = new Role();
        ROLE_USER.setName(RoleEnum.USER.getFullRoleName());
        ROLE_USER.setDescription(RoleEnum.USER.getSimpleRoleName());
        ROLE_USER.setCreationDate(LocalDateTime.now());
        ROLE_USER.setUpdateDate(null);

        // Saving roles
        roleService.save();

        userRequestDto1 = new UserRequestDto();
        userRequestDto1.setFirstName("Hector");
        userRequestDto1.setLastName("Cortez");
        userRequestDto1.setEmail("hector@gmail.com");
        userRequestDto1.setPassword("password");
        userRequestDto1.setRoleId(1L);

        userRequestDto2 = new UserRequestDto();
        userRequestDto2.setFirstName("Francisco");
        userRequestDto2.setLastName("Orieta");
        userRequestDto2.setEmail("fran@gmail.com");
        userRequestDto2.setPassword("password123");
        userRequestDto2.setRoleId(2L);

        user1 = new User();
        user1.setFirstName("Hector");
        user1.setLastName("Cortez");
        user1.setEmail("hector@gmail.com");
        user1.setPassword("password");
        user1.setUpdateDate(null);
        user1.setTransactions(null);
        user1.setFixedTermDeposits(null);

        user2 = new User();
        user2.setFirstName("Francisco");
        user2.setLastName("Orieta");
        user2.setEmail("fran@gmail.com");
        user2.setPassword("password123");
        user2.setUpdateDate(null);
        user2.setTransactions(null);
        user2.setFixedTermDeposits(null);

        // Instantiating accounts
        Account account1, account2, account3, account4;
        account1 = new Account();
        account1.setCurrency(AccountCurrencyEnum.ARS);
        account1.setTransactionLimit(300000.0);
        account1.setBalance(0.0);
        account1.setUpdateDate(null);
        account1.setUser(user1);
        account1.setTransactions(null);
        account1.setFixedTermDeposits(null);

        account2 = new Account();
        account2.setCurrency(AccountCurrencyEnum.USD);
        account2.setTransactionLimit(1000.0);
        account2.setBalance(0.0);
        account2.setUpdateDate(null);
        account2.setUser(user1);
        account2.setTransactions(null);
        account2.setFixedTermDeposits(null);

        account3 = new Account();
        account3.setCurrency(AccountCurrencyEnum.ARS);
        account3.setTransactionLimit(300000.0);
        account3.setBalance(0.0);
        account3.setUpdateDate(null);
        account3.setUser(user2);
        account3.setTransactions(null);
        account3.setFixedTermDeposits(null);

        account4 = new Account();
        account4.setCurrency(AccountCurrencyEnum.USD);
        account4.setTransactionLimit(1000.0);
        account4.setBalance(0.0);
        account4.setUpdateDate(null);
        account4.setUser(user2);
        account4.setTransactions(null);
        account4.setFixedTermDeposits(null);

        accountsUser1 = new ArrayList<>();
        accountsUser1.add(account1);
        accountsUser1.add(account2);

        accountsUser2 = new ArrayList<>();
        accountsUser2.add(account3);
        accountsUser2.add(account4);

        given(roleService.getById(1L)).willReturn(ROLE_ADMIN);
        given(roleService.getById(2L)).willReturn(ROLE_USER);

        ROLE_ADMIN = roleService.getById(1L);
        ROLE_USER = roleService.getById(2L);

        rolesUser1 = new HashSet<>();
        rolesUser1.add(ROLE_ADMIN);

        rolesUser2 = new HashSet<>();
        rolesUser2.add(ROLE_USER);

        user1.setAccounts(accountsUser1);
        user1.setRoles(rolesUser1);

        user2.setAccounts(accountsUser2);
        user2.setRoles(rolesUser2);

        userResponseDto1 = new UserResponseDto();
        userResponseDto1.setId(user1.getId());
        userResponseDto1.setFirstName(user1.getFirstName());
        userResponseDto1.setLastName(user1.getLastName());
        userResponseDto1.setEmail(user1.getEmail());
        userResponseDto1.setPassword(user1.getPassword());
        userResponseDto1.setCreatedAt(user1.getCreationDate());
        userResponseDto1.setUpdatedAt(user1.getUpdateDate());
        userResponseDto1.setRoles(user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        userResponseDto2 = new UserResponseDto();
        userResponseDto2.setId(user2.getId());
        userResponseDto2.setFirstName(user2.getFirstName());
        userResponseDto2.setLastName(user2.getLastName());
        userResponseDto2.setEmail(user2.getEmail());
        userResponseDto2.setPassword(user2.getPassword());
        userResponseDto2.setCreatedAt(user2.getCreationDate());
        userResponseDto2.setUpdatedAt(user2.getUpdateDate());
        userResponseDto2.setRoles(user2.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        authRequestDto = new AuthRequestDto();
        authRequestDto.setEmail("hector@gmail.com");
        authRequestDto.setPassword("password");

        authResponseDto = new AuthResponseDto();
        authResponseDto.setEmail("hector@gmail.com");
        authResponseDto.setToken(jwtUtils.generateToken(
                new org.springframework.security.core.userdetails.User(
                        "hector@gmail.com", "password",
                        rolesUser1.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()))
        );
    }

    @DisplayName("It should SAVE a user")
    @Test
    void itShouldSaveUser() {
        // Given
        given(userRepository.save(user1)).willReturn(user1);
        given(userMapper.dto2Entity(userRequestDto1, rolesUser1)).willReturn(user1);
        given(accountService.createDefaultAccounts(user1)).willReturn(accountsUser1);
        given(underTest.save(userRequestDto1, ROLE_ADMIN)).willReturn(userResponseDto1);

        // When
        UserResponseDto userResponseDtoFromService = underTest.save(userRequestDto1, ROLE_ADMIN);

        // Then
        assertThat(userResponseDtoFromService).isNotNull();
        assertThat(userResponseDtoFromService).usingRecursiveComparison().isEqualTo(userResponseDto1);
    }

    @DisplayName("It should UPDATE a user")
    @Test
    void itShouldUpdateUser() {
        // Given
        given(userRepository.save(user1)).willReturn(user1);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userMapper.dto2Entity(userRequestDto1, rolesUser1)).willReturn(user1);
        given(accountService.createDefaultAccounts(user1)).willReturn(accountsUser1);
        given(underTest.save(userRequestDto1, ROLE_ADMIN)).willReturn(userResponseDto1);
        given(authService.login(authRequestDto)).willReturn(authResponseDto);

        userRequestDto1.setRoleId(null);
        userRequestDto1.setLastName("Armando Cortez");
        userResponseDto1.setLastName(userRequestDto1.getLastName());

        given(underTest.update(1L, userRequestDto1)).willReturn(userResponseDto1);
        given(underTest.getById(1L)).willReturn(user1);
        given(userMapper.refreshValues(userRequestDto1, user1));

        // When
        UserResponseDto userResponseDto = underTest.update(1L, userRequestDto1);

        // Then
        assertThat(userResponseDto).usingRecursiveComparison().isEqualTo(userResponseDto1);
    }
}