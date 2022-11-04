package com.alkemy.wallet.configuration;


import com.alkemy.wallet.model.entity.RoleEnum;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Authentication extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    UserRepository userRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            UserEntity user = userRepository.findByEmail(email);
            if (user != null) {
                if (user.getRole().getName().equals(RoleEnum.ADMIN.getName())) {
                    return new User(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("ADMIN"));
                }
                return new User(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("CLIENT"));
            } else {
                throw new UsernameNotFoundException("Unknown user" + email);
            }
        });
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
