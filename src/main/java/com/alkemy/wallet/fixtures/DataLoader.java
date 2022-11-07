package com.alkemy.wallet.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alkemy.wallet.enumeration.RoleList;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.repository.RoleRepository;

@Component
public class DataLoader implements ApplicationRunner{
    private RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (roleRepository.findByName(RoleList.ADMIN) == null && roleRepository.findByName(RoleList.USER) == null) {
            Role userRole = new Role();
            userRole.setName(RoleList.USER);
            userRole.setDescription("Created user with role USER");
            Role adminRole = new Role();
            adminRole.setName(RoleList.ADMIN);
            adminRole.setDescription("Created user with role ADMIN");
            roleRepository.save(userRole);
            roleRepository.save(adminRole);
        }
    }
}
