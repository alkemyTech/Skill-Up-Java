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
import com.alkemy.wallet.service.IRoleService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.alkemy.wallet.model.constant.FinalValue.PAGE_SIZE;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;
    private final UserMapper mapper;
    private final IAccountService accountService;
    private final IAuthenticationService authService;
    private final IRoleService roleService;

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
        User user = getById(id);
        if (!user.getEmail().equals(authService.getEmailFromContext()))
            throw new AccessDeniedException("Access denied");
        if ((request.getEmail() != null && !request.getEmail().isEmpty()))
            throw new IllegalArgumentException("Cannot modify the email");
        if (request.getRoleId() != null)
            throw new IllegalArgumentException("Cannot modify the role");

        user = mapper.refreshValues(request, user);

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty())
            user.setPassword(authService.encode(request.getPassword()));

        return mapper.entity2Dto(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = getByEmail(authService.getEmailFromContext());
        Role ADMIN = roleService.getById(1L);

        if (user.getRoles().contains(ADMIN)) {
            user = getById(id);
            user.setUpdateDate(LocalDateTime.now());
            user.setDeleted(true);
            return;
        }
        if (!user.getId().equals(id))
            throw new AccessDeniedException("Access denied");

        user.setUpdateDate(LocalDateTime.now());
        user.setDeleted(true);
    }

    @Override
    public UserResponseDto getDetails(Long id) {
        User user = getById(id);
        if (!user.getEmail().equals(authService.getEmailFromContext()))
            throw new AccessDeniedException("Access denied");
        return mapper.entity2Dto(user);
    }

    @Override
    public void addAccount(User user, Account account) {
        user.getAccounts().add(account);
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new NullPointerException(String.format("User not found for id %s", id)));
    }

    @Override
    public User getByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public Page<UserResponseDto> findAll(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        pageable.next().getPageNumber();
        return repository.findAll(pageable).map(mapper::entity2Dto);
    }
}
