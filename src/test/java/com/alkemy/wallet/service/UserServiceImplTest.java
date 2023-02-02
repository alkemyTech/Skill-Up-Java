package com.alkemy.wallet.service;

import com.alkemy.wallet.config.AppConfig;
import com.alkemy.wallet.model.constant.AccountCurrencyEnum;
import com.alkemy.wallet.model.constant.RoleEnum;
import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.request.UserUpdateRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import com.alkemy.wallet.utils.CustomMessageSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@Import(AppConfig.class)
@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    protected IUserRepository userRepository;
    @Mock
    protected UserMapper userMapper;
    @Mock
    protected IAccountService accountService;
    @Mock
    protected BCryptPasswordEncoder passwordEncoder;
    @Mock
    protected IRoleService roleService;
    @Mock
    protected CustomMessageSource messageSource;

    @Captor
    protected ArgumentCaptor<User> argumentCaptor;
    @InjectMocks
    protected UserServiceImpl underTest;

    protected UserRequestDto userRequestDto1;
    protected UserRequestDto userRequestDto2;
    protected UserUpdateRequestDto userUpdateRequestDto;
    protected UserResponseDto userResponseDto1;
    protected UserResponseDto userResponseDto2;
    protected User user1;
    protected User user2;
    protected User user3;
    protected Role ROLE_ADMIN;
    protected Role ROLE_USER;
    protected Role ROLE_USER_2;
    protected List<Account> accountsUser1;
    protected List<Account> accountsUser2;
    protected Account accountUser3;


    @BeforeEach
    void setUp() {
        // Instantiating each user
        user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Hector");
        user1.setLastName("Cortez");
        user1.setEmail("hector@gmail.com");
        user1.setPassword("password");
        user1.setUpdateDate(null);
        user1.setTransactions(null);
        user1.setFixedTermDeposits(null);

        user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Francisco");
        user2.setLastName("Orieta");
        user2.setEmail("fran@gmail.com");
        user2.setPassword("password123");
        user2.setUpdateDate(null);
        user2.setTransactions(null);
        user2.setFixedTermDeposits(null);

        user3 = new User();
        user3.setId(3L);
        user3.setFirstName("Lucila");
        user3.setLastName("Orieta");
        user3.setEmail("lucila@gmail.com");
        user3.setPassword("password321");
        user3.setUpdateDate(null);
        user3.setTransactions(null);
        user3.setFixedTermDeposits(null);

        // Instantiating roles
        ROLE_ADMIN = new Role();
        ROLE_ADMIN.setId(1L);
        ROLE_ADMIN.setName(RoleEnum.ADMIN);
        ROLE_ADMIN.setCreationDate(now());
        ROLE_ADMIN.setUpdateDate(null);
        ROLE_ADMIN.setUser(user1);

        ROLE_USER = new Role();
        ROLE_USER.setId(2L);
        ROLE_USER.setName(RoleEnum.USER);
        ROLE_USER.setCreationDate(now());
        ROLE_USER.setUpdateDate(null);
        ROLE_USER.setUser(user2);

        ROLE_USER_2 = new Role();
        ROLE_USER_2.setId(3L);
        ROLE_USER_2.setName(RoleEnum.USER);
        ROLE_USER_2.setCreationDate(now());
        ROLE_USER_2.setUpdateDate(null);
        ROLE_USER_2.setUser(user3);

        // Saving roles
        roleService.saveNewRole(RoleEnum.ADMIN.getSimpleRoleName(), user1);
        roleService.saveNewRole(RoleEnum.USER.getSimpleRoleName(), user2);
        roleService.saveNewRole(RoleEnum.USER.getSimpleRoleName(), user3);

        // Setting up the roles for each user
        user1.setAuthorities(Collections.singleton(ROLE_ADMIN));
        user2.setAuthorities(Collections.singleton(ROLE_USER));
        user3.setAuthorities(Collections.singleton(ROLE_USER_2));

        // Instantiating the DTOs requests
        userRequestDto1 = new UserRequestDto();
        userRequestDto1.setFirstName("Hector");
        userRequestDto1.setLastName("Cortez");
        userRequestDto1.setEmail("hector@gmail.com");
        userRequestDto1.setPassword("password");
        userRequestDto1.setRole(RoleEnum.ADMIN.getSimpleRoleName());

        userRequestDto2 = new UserRequestDto();
        userRequestDto2.setFirstName("Francisco");
        userRequestDto2.setLastName("Orieta");
        userRequestDto2.setEmail("fran@gmail.com");
        userRequestDto2.setPassword("password123");
        userRequestDto2.setRole(RoleEnum.USER.getSimpleRoleName());

        // Instantiating accounts
        Account account1, account2, account3, account4;
        account1 = new Account();
        account1.setId(1L);
        account1.setCurrency(AccountCurrencyEnum.ARS);
        account1.setTransactionLimit(300000.0);
        account1.setBalance(0.0);
        account1.setUpdateDate(null);
        account1.setUser(user1);
        account1.setTransactions(null);
        account1.setFixedTermDeposits(null);

        account2 = new Account();
        account2.setId(2L);
        account2.setCurrency(AccountCurrencyEnum.USD);
        account2.setTransactionLimit(1000.0);
        account2.setBalance(0.0);
        account2.setUpdateDate(null);
        account2.setUser(user1);
        account2.setTransactions(null);
        account2.setFixedTermDeposits(null);

        account3 = new Account();
        account3.setId(3L);
        account3.setCurrency(AccountCurrencyEnum.ARS);
        account3.setTransactionLimit(300000.0);
        account3.setBalance(0.0);
        account3.setUpdateDate(null);
        account3.setUser(user2);
        account3.setTransactions(null);
        account3.setFixedTermDeposits(null);

        account4 = new Account();
        account4.setId(4L);
        account4.setCurrency(AccountCurrencyEnum.USD);
        account4.setTransactionLimit(1000.0);
        account4.setBalance(0.0);
        account4.setUpdateDate(null);
        account4.setUser(user2);
        account4.setTransactions(null);
        account4.setFixedTermDeposits(null);

        accountUser3 = new Account();
        accountUser3.setId(5L);
        accountUser3.setCurrency(AccountCurrencyEnum.USD);
        accountUser3.setTransactionLimit(1000.0);
        accountUser3.setBalance(0.0);
        accountUser3.setUpdateDate(null);
        accountUser3.setUser(user3);
        accountUser3.setTransactions(null);
        accountUser3.setFixedTermDeposits(null);

        // Adding each account to their respective lists
        accountsUser1 = new ArrayList<>();
        accountsUser1.add(account1);
        accountsUser1.add(account2);

        accountsUser2 = new ArrayList<>();
        accountsUser2.add(account3);
        accountsUser2.add(account4);

        // Setting accounts to users
        user1.setAccounts(accountsUser1);
        user2.setAccounts(accountsUser2);

        // Instantiating DTOs
        userResponseDto1 = new UserResponseDto();
        userResponseDto1.setId(user1.getId());
        userResponseDto1.setFirstName(user1.getFirstName());
        userResponseDto1.setLastName(user1.getLastName());
        userResponseDto1.setEmail(user1.getEmail());
        userResponseDto1.setPassword(user1.getPassword());
        userResponseDto1.setCreatedAt(user1.getCreationDate());
        userResponseDto1.setUpdatedAt(user1.getUpdateDate());
        userResponseDto1.setAuthorities(user1.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));

        userResponseDto2 = new UserResponseDto();
        userResponseDto2.setId(user2.getId());
        userResponseDto2.setFirstName(user2.getFirstName());
        userResponseDto2.setLastName(user2.getLastName());
        userResponseDto2.setEmail(user2.getEmail());
        userResponseDto2.setPassword(user2.getPassword());
        userResponseDto2.setCreatedAt(user2.getCreationDate());
        userResponseDto2.setUpdatedAt(user2.getUpdateDate());
        userResponseDto2.setAuthorities(user2.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));

        userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setFirstName("Hector Armando");
        userUpdateRequestDto.setLastName("Cortez");
        userUpdateRequestDto.setPassword("password123");
    }

    @DisplayName("It should SAVE a user")
    @Test
    void itShouldSaveUser() {
        // Given
        given(userRepository.save(user1)).willReturn(user1);
        given(userMapper.dto2Entity(userRequestDto1)).willReturn(user1);
        given(accountService.createDefaultAccounts(user1)).willReturn(accountsUser1);
        given(underTest.saveNewUser(userRequestDto1)).willReturn(userResponseDto1);

        // When
        UserResponseDto userResponseDtoFromService = underTest.saveNewUser(userRequestDto1);

        // Then
        assertThat(userResponseDtoFromService).isNotNull();
        assertThat(userResponseDtoFromService).usingRecursiveComparison().isEqualTo(userResponseDto1);
    }

    @DisplayName("It should UPDATE a user")
    @Test
    void itShouldUpdateUser() {
        // Given
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(underTest.updateUser(1L, user1, userUpdateRequestDto)).willReturn(userResponseDto1);

        // When
        UserResponseDto userResponseDtoFromService = underTest.updateUser(1L, user1, userUpdateRequestDto);

        // Then
        assertThat(userResponseDtoFromService).isNotNull();
        assertThat(userResponseDtoFromService).usingRecursiveComparison().isEqualTo(userResponseDto1);
    }

    @DisplayName("It should NEVER UPDATE a user")
    @Test
    void itShouldNeverUpdateUser() {
        // Given
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));
        given(messageSource.message("user.access-denied", null)).willReturn("Access denied");

        // When
        // Then
        assertNotNull(messageSource);
        assertThatThrownBy(() -> underTest.updateUser(2L, user1, userUpdateRequestDto))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Access denied");

        // Finally
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    @DisplayName("An ADMIN should DELETE any user")
    public void itShouldDeleteAnyUser() {
        // Given
        user2.setId(2L);
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));

        // When
        underTest.deleteUserById(2L, user1);

        // Then
        then(userRepository).should().save(argumentCaptor.capture());
        User userArgumentCapture = argumentCaptor.getValue();
        assertThat(userArgumentCapture.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("A USER should only DELETE himself")
    public void itShouldDeleteHimself() {
        // Given
        user2.setId(2L);
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));

        // When
        underTest.deleteUserById(2L, user2);

        // Then
        then(userRepository).should().save(argumentCaptor.capture());
        User userArgumentCapture = argumentCaptor.getValue();
        assertThat(userArgumentCapture.isEnabled()).isFalse();
        assertThat(userArgumentCapture.getId()).isEqualTo(user2.getId());
    }

    @Test
    @DisplayName("A non ADMIN user should not DELETE other user")
    public void itShouldThrowAccessDeniedForDelete() {
        // Given
        user1.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(messageSource.message("user.access-denied", null)).willReturn("Access denied");

        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteUserById(1L, user2))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Access denied");

        // Finally
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    @DisplayName("It should FIND a user by ID")
    public void itShouldFindUserById() {
        // Given
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));

        // When
        User userFound = underTest.getUserById(2L);

        // Then
        assertThat(userFound).usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    @DisplayName("It should NOT FIND a user and THROW")
    public void itShouldNotFindUserAndThrow() {
        // Given
        given(userRepository.findById(3L)).willReturn(Optional.empty());
        given(messageSource.message("entity.not-found", new String[]{"User", "id", Long.toString(3L)}))
                .willReturn("User not found for id 3");

        // When
        // Then
        assertThatThrownBy(() -> underTest.getUserById(3L))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User not found for id 3");
    }

    @Test
    @DisplayName("It should GET user by email")
    public void itShouldGetUserByEmail() {
        // Given
        String email = "hector@gmail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user1));

        // When
        User userFound = underTest.getUserByEmail(email);

        // Then
        assertThat(userFound).isNotNull();
        assertThat(userFound).usingRecursiveComparison().isEqualTo(user1);
    }

    @Test
    @DisplayName("It should GET null when find by email")
    public void itShouldGetNull() {
        // Given
        String email = "non-valid-email@gmail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // When
        User userFound = underTest.getUserByEmail(email);

        // Then
        assertThat(userFound).isNull();
    }

    @Test
    @DisplayName("It should GET TRUE if email exists")
    public void itShouldGetTrueForEmail() {
        // Given
        String email = "fran@gmail.com";
        given(userRepository.selectExistsEmail(email)).willReturn(Boolean.TRUE);

        // When
        boolean emailExists = underTest.checkIfUserEmailExists(email);

        // Then
        assertThat(emailExists).isTrue();
    }

    @Test
    @DisplayName("It should GET FALSE if email NOT exists")
    public void itShouldGetFalseForEmail() {
        // Given
        String email = "non-valid-email@gmail.com";
        given(userRepository.selectExistsEmail(email)).willReturn(Boolean.FALSE);

        // When
        boolean emailExists = underTest.checkIfUserEmailExists(email);

        // Then
        assertThat(emailExists).isFalse();
    }

    @Test
    @DisplayName("It should GET user details")
    public void itShouldGetUserDetails() {
        // Given
        Long USER_ID = 2L;
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user1));
        given(userMapper.entity2Dto(user1)).willReturn(userResponseDto1);

        // When
        UserResponseDto expected = underTest.getUserDetails(USER_ID, user1);

        // Then
        assertThat(expected).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(userResponseDto1);
    }

    @Test
    @DisplayName("It should NOT GET user details")
    public void itShouldGetNotUserDetailsAndThrow() {
        // Given
        Long USER_ID = 2L;
        String EXCEPTION_MESSAGE = "Access denied";
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user2));
        given(messageSource.message("user.access-denied", null)).willReturn(EXCEPTION_MESSAGE);

        // When
        // Then
        assertThatThrownBy(() -> underTest.getUserDetails(USER_ID, user1))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("It should GET ALL users")
    public void itShouldGetAllUsers() {
        // Given
        Page<User> pageableUsers = new PageImpl<>(List.of(user1, user2));
        Page<UserResponseDto> pageableUsersDto = new PageImpl<>(Arrays.asList(userResponseDto1, userResponseDto2));

        given(userRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).willReturn(pageableUsers);
        given(userMapper.entity2Dto(user1)).willReturn(userResponseDto1);
        given(userMapper.entity2Dto(user2)).willReturn(userResponseDto2);

        // When
        Page<UserResponseDto> listToCheck = underTest.getAllUsers(0);

        // Then
        assertThat(listToCheck).usingRecursiveComparison().isEqualTo(pageableUsersDto);
    }

    /*@Test
    @DisplayName("It should ADD accounts to user")
    public void itShouldGetAddAccounts() {
        // Given

        // When
        underTest.addAccountToUser(user3, accountUser3);

        // Then
        then(userRepository).should().save(argumentCaptor.capture());
        User userArgumentCapture = argumentCaptor.getValue();
        Optional<Account> accountExpected = userArgumentCapture.getAccounts()
                .stream()
                .filter(account -> account.getId() != null)
                .findFirst();
        assertThat(accountExpected.isPresent()).isTrue();
        assertThat(accountExpected.get()).usingRecursiveComparison().isEqualTo(accountUser3);
    }*/
}