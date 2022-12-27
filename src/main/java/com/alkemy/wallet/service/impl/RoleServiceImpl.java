package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.IRoleService;
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.Optional;

import static com.alkemy.wallet.model.constant.RoleEnum.ADMIN;
import static com.alkemy.wallet.model.constant.RoleEnum.USER;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;
    private final CustomMessageSource messageSource;

    @Override
    public Role saveNewRole(String roleName, User user) {
        Role role = new Role();

        if (roleName.equalsIgnoreCase(ADMIN.getSimpleRoleName())) {
            role.setName(ADMIN.getFullRoleName());
            role.setDescription(ADMIN.getSimpleRoleName());
            role.setUser(user);
        } else if (roleName.equalsIgnoreCase(USER.getSimpleRoleName())) {
            role.setName(USER.getFullRoleName());
            role.setDescription(USER.getSimpleRoleName());
            role.setUser(user);
        } else {
            throw new InputMismatchException(messageSource.message("role.mismatch", null));
        }

        roleRepository.save(role);
        return role;
    }
}
