package com.alkemy.wallet.util;

import com.alkemy.wallet.dto.RoleDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.service.CustomUserDetailsService;
import com.alkemy.wallet.service.interfaces.IRoleService;
import com.alkemy.wallet.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    CommandLineRunner commandLineRunner() throws ResourceNotFoundException {
        return args -> {

            RoleDto role;
            role = RoleDto.builder()
                    .name(RoleName.ROLE_USER).build();
            roleService.createRole(role);

            role = RoleDto.builder()
                    .name(RoleName.ROLE_ADMIN).build();
            roleService.createRole(role);
        };
    }
}
