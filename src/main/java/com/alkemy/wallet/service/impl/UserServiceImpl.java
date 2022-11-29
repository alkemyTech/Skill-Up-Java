package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;
    private final UserMapper mapper;
    private final IAccountService accountService;
    private final IAuthenticationService authService;

    @Override
    public UserResponseDto save(UserRequestDto request, Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = mapper.dto2Entity(request, roles);
        user.setPassword(authService.encode(user.getPassword()));
        user.setDeleted(false);
        user.setAccounts(accountService.createDefaultAccounts(user));
        return mapper.entity2Dto(repository.save(user));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto request) {
        User loggedUser = getByEmail(authService.getEmailFromContext());

        if (!loggedUser.getId().equals(id))
            throw new AccessDeniedException("Access denied");
        if ((request.getEmail() != null && !request.getEmail().isEmpty()))
            throw new IllegalArgumentException("Cannot modify the email");
        if (request.getRoleId() != null)
            throw new IllegalArgumentException("Cannot modify the role");

        loggedUser = mapper.refreshValues(request, loggedUser);
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty())
            loggedUser.setPassword(authService.encode(request.getPassword()));

        return mapper.entity2Dto(repository.save(loggedUser));
    }

    @Override
    public User getByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public void addAccount(User user, Account account) {
        user.getAccounts().add(account);
        repository.save(user);
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new EntityNotFoundException(String.format("User not found for id %s", id)));
    }

    @Override
    public UserResponseDto getDetails(Long id) {
        User loggedUser = getByEmail(authService.getEmailFromContext());
        if (!loggedUser.getId().equals(id))
            throw new AccessDeniedException("Access denied");
        return mapper.entity2Dto(loggedUser);
    }

    @Override
    public void deleteById(Long id) {
        User loggedUser = getByEmail(authService.getEmailFromContext());
        Role ADMIN = loggedUser.getRoles().stream().filter(role -> role.getName().equals("ROLE_ADMIN")).findFirst().orElse(null);
        Role USER = loggedUser.getRoles().stream().filter(role -> role.getName().equals("ROLE_USER")).findFirst().orElse(null);
        if (loggedUser.getRoles().contains(ADMIN)) {
            User dbUser = getById(id);
            dbUser.setUpdateDate(LocalDateTime.now());
            repository.save(dbUser);
            repository.delete(dbUser);
        } else if (loggedUser.getRoles().contains(USER)) {
            User dbUser = getById(id);
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
