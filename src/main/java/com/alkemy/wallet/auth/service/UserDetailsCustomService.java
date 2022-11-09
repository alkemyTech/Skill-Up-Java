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

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsCustomService implements UserDetailsService {

    private final IUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> dbResponse = repository.findByEmail(email);
        if (dbResponse.isEmpty())
            throw new UsernameNotFoundException(String.format("User not found for email %s", email));
        return new org.springframework.security.core.userdetails.User(dbResponse.get().getEmail(), dbResponse.get().getPassword(), mapRolesToGrantedAuth(dbResponse.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToGrantedAuth(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
