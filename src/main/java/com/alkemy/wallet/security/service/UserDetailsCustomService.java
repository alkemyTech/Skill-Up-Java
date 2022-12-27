package com.alkemy.wallet.security.service;

import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsCustomService implements UserDetailsService {

    private final IUserRepository repository;
    private final CustomMessageSource messageSource;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException(messageSource
                    .message("entity.not-found", new String[] {"User", "email", email}));
        if (user.get().isDeleted())
            throw new DisabledException(messageSource.message("user.disabled-account", null));
        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), user.get().getPassword(), mapRoleToGrantedAuth(user.get().getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRoleToGrantedAuth(Role role) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }
}
