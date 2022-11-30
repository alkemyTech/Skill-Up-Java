package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.alkemy.wallet.model.constant.RoleEnum.ADMIN;
import static com.alkemy.wallet.model.constant.RoleEnum.USER;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository repository;

    @Override
    public void save() {
        repository.save(new Role(1L, ADMIN.getFullRoleName(), ADMIN.getSimpleRoleName(), now(), null));
        repository.save(new Role(2L, USER.getFullRoleName(), USER.getSimpleRoleName(), now(), null));
    }

    @Override
    public Role getById(Long id) {
        Optional<Role> response = repository.findById(id);
        return response.orElseThrow(() ->
                new NullPointerException(String.format("Invalid role id: %s. Try ADMIN(1) or USER(2)", id)));
    }

    @Override
    public List<Role> getAll() {
        return repository.findAll();
    }
}
