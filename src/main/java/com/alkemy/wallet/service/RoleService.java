package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.RoleDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.IRoleRepository;
import com.alkemy.wallet.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public RoleDto findByName(RoleName roleName) {
        Optional<Role> user = roleRepository.findByName(roleName);
        if (user.isPresent()) {
            return mapper.getMapper().map(user, RoleDto.class);
        }
        throw new ResourceNotFoundException("Role name not found");
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = mapper.getMapper().map(roleDto, Role.class);
        return mapper.getMapper().map(roleRepository.save(role), RoleDto.class);
    }

}
