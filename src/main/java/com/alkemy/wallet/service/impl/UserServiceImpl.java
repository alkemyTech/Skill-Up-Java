package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.model.request.UserRequestDto;
import com.alkemy.wallet.model.response.UserResponseDto;
import com.alkemy.wallet.model.response.list.UserListResponseDto;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;

    private final UserMapper mapper;

    private final IAuthService authService;

    @Override
    public UserResponseDto update(Long id, String token, UserRequestDto request) {
        User userFromToken = authService.getUserFromToken(token);
        User dbUser = getEntityById(id);
        if (!userFromToken.equals(dbUser))
            throw new AccessDeniedException("Access denied");
        if ((request.getEmail() != null && !request.getEmail().isEmpty()) || request.getRoleId() != null)
            throw new IllegalArgumentException("Cannot modify the email and the role");
        dbUser = mapper.refreshValues(request, dbUser);
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty())
            dbUser.setPassword(authService.encode(request.getPassword()));
        return mapper.entity2Dto(repository.save(dbUser));
    }

    @Override
    public User getEntityById(Long id) {
        Optional<User> dbResponse = repository.findById(id);
        if (dbResponse.isEmpty())
            throw new EntityNotFoundException(String.format("User not found for id %s", id));
        return dbResponse.get();
    }

    @Override
    public UserResponseDto getUserDetails(Long id, String token) {
        User userFromToken = authService.getUserFromToken(token);
        User dbUser = getEntityById(id);
        if (!userFromToken.equals(dbUser))
            throw new AccessDeniedException("Access denied");
        return mapper.entity2Dto(dbUser);
    }

    @Override
    public UserListResponseDto getUsers() {
        UserListResponseDto users = mapper.entityList2DtoList(repository.findAll());
        if (users.getUsers().isEmpty())
            throw new IllegalArgumentException(String.format("List is empty or null: %s", users.getUsers()));
        return users;
    }

    @Override
    public UserResponseDto deleteUserById(Long userId) {
        if (repository.existsById(userId)) {
            User deletedUser = repository.findById(userId).get();
            deletedUser.setUpdateDate(LocalDateTime.now());
            repository.save(deletedUser);
            deletedUser.setSoftDelete(true);

            repository.deleteById(userId);
            return mapper.entity2Dto(deletedUser);
        } else
            throw new EntityNotFoundException(String.format("User with id: %s was not found or was already deleted", userId.toString()));
    }

    //Todo: Implementar metodo save y findById
    @Override
    public User save(User accountUser) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<UserResponseDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        pageable.next().getPageNumber();
        return repository.findAll(pageable).map(mapper::entity2Dto);
    }
}
