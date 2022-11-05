package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.RoleDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.RoleEntity;
import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
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

    public UserEntity createUser(UserRequestDTO user, RoleDTO role) {

        Optional<RoleEntity> roleEntity = roleRepository.getRoleById(role.getId());
        RoleEntity roleEntityResponse = null;
        if(!roleEntity.isPresent()) {
            roleEntityResponse = RoleEntity.builder()
                    .roleId(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .build();
            roleRepository.saveAndFlush(roleEntityResponse);
        }

      UserEntity userEntity = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(roleEntityResponse)
                .build();

        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public TransactionEntity saveDeposit(TransactionDTO transaction){
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .build();
        return transactionRepository.saveAndFlush(transactionEntity);
    }

    public AccountEntity createAccount(AccountDTO accountDTO, UserEntity userEntity){
        AccountEntity accountEntity = AccountEntity.builder()
                .currency(accountDTO.getCurrency())
                .transactionLimit(accountDTO.getTransactionLimit())
                .balance(0.0)
                .user(userEntity)
                .build();
        return accountRepository.saveAndFlush(accountEntity);
    }
}
