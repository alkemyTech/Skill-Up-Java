package com.alkemy.wallet.auth.service;

import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserDetailsCustomService implements UserDetailsService {

    private final IUserRepository repository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException(String.format("User not found for email %s", email));
        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), user.get().getPassword(), mapRoleToGrantedAuth(user.get().getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRoleToGrantedAuth(Role role) {
        return Stream.of().map((object -> new SimpleGrantedAuthority(role.getName()))).toList();
    }
}
