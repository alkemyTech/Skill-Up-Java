package com.alkemy.wallet.security.service;

import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsCustomService implements UserDetailsService {

    private final IUserRepository repository;
    private final CustomMessageSource messageSource;

    @Transactional
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException(messageSource
                    .message("entity.not-found", new String[] {"User", "email", email}));
        if (!user.get().isEnabled())
            throw new DisabledException(messageSource.message("user.disabled-account", null));
        return User.builder()
                .email(user.get().getEmail())
                .password(user.get().getPassword())
                .authorities(user.get().getAuthorities())
                .build();
    }
}
