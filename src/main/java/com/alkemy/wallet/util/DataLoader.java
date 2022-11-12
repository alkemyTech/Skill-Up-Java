package com.alkemy.wallet.util;

import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.RoleName;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.RoleRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.UserService;
import com.alkemy.wallet.service.implementation.UserServiceImpl;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            List<Role> roleList = new ArrayList<Role>();

            roleList = roleRepository.findAll();


            if(roleList.size()<2) {

                    Role role1 = new Role(1, RoleName.USER, "Users rol", new Timestamp(System.currentTimeMillis()), null);
                    roleRepository.save(role1);

                    Role role2 = new Role(2,RoleName.ADMIN, "Admins rol", new Timestamp( System.currentTimeMillis() ), null );
                    roleRepository.save(role2);

            }

            if(userRepository.findByEmail("admin")!=null)
                return;

            Role adminRole = roleRepository.findById(2).orElse(new Role(2,RoleName.ADMIN, "Admins rol", new Timestamp( System.currentTimeMillis() ), null ));

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passEncoded = passwordEncoder.encode("admin");

            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin");
            admin.setPassword(passEncoded);
            admin.setRole(adminRole);
            admin.setCreationDate(new Timestamp(System.currentTimeMillis()));
            admin.setSoftDelete(false);

            userRepository.save(admin);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
