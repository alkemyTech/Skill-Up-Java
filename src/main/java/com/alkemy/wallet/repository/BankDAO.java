package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.UnionSubclass;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BankDAO {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final RoleRepository roleRepository;

    private final TransactionRepository transactionRepository;

    private final FixedTermDepositRepository fixedTermDepositRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserRequestDTO user) {
        UserEntity userEntity = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return userRepository.saveAndFlush(userEntity);
    }

}
