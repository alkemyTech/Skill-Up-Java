/*
package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.model.dto.response.list.UserListResponseDto;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    private final IUserRepository repository = Mockito.mock(IUserRepository.class);
    private final UserMapper mapper = new UserMapper();
    private final IAuthenticationService authService = Mockito.mock(IAuthenticationService.class);
    private final IUserService service = new UserServiceImpl(repository, mapper, authService);
    private final UserController userController = new UserController(service);

    private List<User> users1;
    private List<User> users2;

    @BeforeEach
    void setUp() {
        Set<Role> roles = new HashSet<>();
        users1 = new ArrayList<>();
        users2 = new ArrayList<>();

        roles.add(new Role(1L, "ADMIN", "Rol admin", LocalDateTime.now(), null));

        users1.add(new User(1L, "admin", "admin", "admin@gmail.com",
                "$10$HqJv0Vzw0u6WzPss7JTTzuYazYo7qaQEjUBPkKSauSAlKeMyXt0Dm", LocalDateTime.now(),
                null, false, null, roles, null, null));

        users2.add(new User(1L, "admin", "admin", "admin@gmail.com",
                "$10$HqJv0Vzw0u6WzPss7JTTzuYazYo7qaQEjUBPkKSauSAlKeMyXt0Dm", LocalDateTime.now(),
                null, false, null, roles, null, null));
    }

    @Test
    void getUsers() {
        Mockito.when(repository.findAll()).thenReturn(users1);

        ResponseEntity<UserListResponseDto> response = userController.getUsers();
        UserListResponseDto listResponseDto = mapper.entityList2DtoList(users2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ResponseEntity<>(listResponseDto, HttpStatus.OK), response);
    }
}*/
