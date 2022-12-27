package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.UserRequestDto;
import com.alkemy.wallet.model.dto.request.UserUpdateRequestDto;
import com.alkemy.wallet.model.dto.response.UserResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.UserMapper;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.IRoleService;
import com.alkemy.wallet.service.IUserService;
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.alkemy.wallet.model.constant.RoleEnum.ADMIN;
import static com.alkemy.wallet.utils.PageUtil.PAGE_SIZE;
import static java.time.LocalDateTime.now;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final IAccountService accountService;
    private final IAuthService authService;
    private final IRoleService roleService;
    private final CustomMessageSource messageSource;

    @Override
    public UserResponseDto saveNewUser(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(authService.encode(userRequestDto.getPassword()));
        User user = userMapper.dto2Entity(userRequestDto);
        Role role = roleService.saveNewRole(userRequestDto.getRole(), user);
        user.setRole(role);
        user.setAccounts(accountService.createDefaultAccounts(user));
        return userMapper.entity2Dto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = getUserById(id);
        if (!user.getEmail().equals(authService.getEmailFromContext()))
            throw new AccessDeniedException(messageSource.message("user.access-denied", null));
        User userUpdated = userMapper.refreshValues(userUpdateRequestDto, user);
        userUpdated.setUpdateDate(now());
        return userMapper.entity2Dto(userUpdated);
    }

    @Override
    public void deleteUserById(Long id) {
        User loggedUser, user;
        loggedUser = getUserByEmail(authService.getEmailFromContext());

        if (loggedUser.getRole().getName().equals(ADMIN.getFullRoleName())) {
            user = getUserById(id);
            user.setUpdateDate(now());
            user.setDeleted(true);
            return;
        }

        if (!loggedUser.getId().equals(id))
            throw new AccessDeniedException(messageSource.message("user.access-denied", null));
        loggedUser.setUpdateDate(now());
        loggedUser.setDeleted(true);
    }

    @Override
    public UserResponseDto getUserDetails(Long id) {
        User user = getUserById(id);
        if (!user.getEmail().equals(authService.getEmailFromContext()))
            throw new AccessDeniedException(messageSource.message("user.access-denied", null));
        return userMapper.entity2Dto(user);
    }

    @Override
    public void addAccountToUser(User user, Account account) {
        user.getAccounts().add(account);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new NullPointerException(messageSource
                .message("entity.not-found", new String[]{"User", "id", id.toString()})));
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public boolean checkIfUserEmailExists(String email) {
        return userRepository.selectExistsEmail(email);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        pageable.next().getPageNumber();
        return userRepository.findAll(pageable).map(userMapper::entity2Dto);
    }
}
