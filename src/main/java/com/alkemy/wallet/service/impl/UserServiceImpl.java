package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
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
        if (!userFromToken.getId().equals(id))
            throw new AccessDeniedException("Access denied");

        User dbUser = getEntityById(id);
        if ((request.getEmail() != null && !request.getEmail().isEmpty()) || request.getRoleId() != null)
            throw new IllegalArgumentException("Cannot modify the email or the role");

        dbUser = mapper.refreshValues(request, dbUser);

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty())
            dbUser.setPassword(authService.encode(request.getPassword()));

        return mapper.entity2Dto(repository.save(dbUser));
    }

    @Override
    public void addAccount(User user, Account account) {
        user.getAccounts().add(account);
        repository.save(user);
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
        User loggedUser = authService.getUserFromToken(token);
        if (!loggedUser.getId().equals(id))
            throw new AccessDeniedException("Access denied");
        loggedUser = getEntityById(id);
        return mapper.entity2Dto(loggedUser);
    }

    @Override
    public void deleteUserById(Long id, String token) {
        User loggedUser = authService.getUserFromToken(token);
        Role ADMIN_ROLE = authService.getRoleById(2L);
        Role USER_ROLE = authService.getRoleById(1L);

        if (loggedUser.getRoles().contains(ADMIN_ROLE)) {
            User dbUser = getEntityById(id);
            dbUser.setUpdateDate(LocalDateTime.now());
            repository.save(dbUser);
            repository.delete(dbUser);
        } else if (loggedUser.getRoles().contains(USER_ROLE) && loggedUser.getId().equals(id)) {
            User dbUser = getEntityById(id);
            dbUser.setUpdateDate(LocalDateTime.now());
            repository.save(dbUser);
            repository.delete(dbUser);
        } else {
            throw new AccessDeniedException("Access Denied");
        }
    }

    @Override
    public Page<UserResponseDto> findAll(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);
        pageable.next().getPageNumber();
        return repository.findAll(pageable).map(mapper::entity2Dto);
    }
}
