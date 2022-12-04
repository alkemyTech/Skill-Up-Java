package com.alkemy.wallet.util;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.enumeration.RoleList;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.RoleRepository;
import com.alkemy.wallet.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner{

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, AccountRepository accountRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
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
        if (userRepository.findByEmail("superuser@springbugs.com") == null) {
            User superUser = new User();
            superUser.setFirstName("Super");
            superUser.setLastName("User");
            superUser.setEmail("superuser@springbugs.com");
            superUser.setPassword("admin");
            Role adminUserRole = roleRepository.findByName(RoleList.ADMIN);
            superUser.setRole(adminUserRole);
            userRepository.save(superUser); 
        }

        // The next fixture is useless for production environment
        if(userRepository.findByEmail("sampleuser1@springbugs.com") == null &&
            userRepository.findByEmail("sampleuser2@springbugs.com") == null &&
            userRepository.findByEmail("sampleuser3@springbugs.com") == null) {
            Random random = new Random();
            Double USDLow = 10.0;
            Double USDHigh = 1000.0;
            Double ARSLow = 1000.0;
            Double ARSHigh = 100000.0;
            for (int i = 1; i <= 3; i ++) {
                User user = new User();
                user.setFirstName("Sample");
                user.setLastName("User" + i);
                user.setEmail("sampleuser" + i + "@springbugs.com");
                user.setPassword("user");
                Role userRole = roleRepository.findByName(RoleList.USER);
                user.setRole(userRole);
                userRepository.save(user); 
            }
            for (int i = 1; i <= 3; i ++) {
                Double USDBalance = random.nextDouble(USDHigh-USDLow) + USDLow;
                Account accountUSD = new Account();
                accountUSD.setCurrency(CurrencyList.USD);
                accountUSD.setBalance(USDBalance);
                accountUSD.setTransactionLimit(1000.0);
                accountUSD.setUser(userRepository.findByEmail("sampleuser" + i + "@springbugs.com"));
                accountRepository.save(accountUSD);

                Double ARSBalance = random.nextDouble(ARSHigh-ARSLow) + ARSLow;
                Account accountARS = new Account();
                accountARS.setCurrency(CurrencyList.ARS);
                accountARS.setBalance(ARSBalance);
                accountARS.setTransactionLimit(300000.0);
                accountARS.setUser(userRepository.findByEmail("sampleuser" + i + "@springbugs.com"));
                accountRepository.save(accountARS);
            }
        }
    }
}
