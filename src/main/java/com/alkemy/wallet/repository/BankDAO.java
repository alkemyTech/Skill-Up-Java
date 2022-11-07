package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public AccountEntity getAccount(Long userId, String currency) {
        return accountRepository.getAccount(userId, currency);
    }

    public Optional<AccountEntity> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public void deleteByUserId(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<IBalance> getBalance() {
        return transactionRepository.getBalance("1");
    }

    public RoleEntity getRole(RoleDTO role) {
        Optional<RoleEntity> roleEntity = roleRepository.findById(role.getId());
        if (!roleEntity.isPresent()) {
            RoleEntity roleEntityResponse = RoleEntity.builder()
                    .roleId(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .build();
            roleRepository.saveAndFlush(roleEntityResponse);
            return roleEntityResponse;
        }
        return roleEntity.get();
    }

    public UserEntity createUser(UserRequestDTO user, RoleDTO role) {
        RoleEntity roleEntity = getRole(role);
        UserEntity userEntity = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(roleEntity)
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return userRepository.saveAndFlush(userEntity);
    }

    public AccountEntity createAccount(AccountDTO accountDTO, UserEntity userEntity) {
        AccountEntity accountEntity = AccountEntity.builder()
                .currency(accountDTO.getCurrency())
                .transactionLimit(accountDTO.getTransactionLimit())
                .balance(0.0)
                .user(userEntity)
                .build();
        return accountRepository.saveAndFlush(accountEntity);
    }

    public TransactionEntity createTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .type(type)
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .account(accountEntity)
                .build();
        return transactionRepository.saveAndFlush(transactionEntity);
    }

    public FixedTermDepositEntity createFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit, AccountEntity accountEntity, UserEntity userEntity) {
        FixedTermDepositEntity fixedTermDepositEntity = FixedTermDepositEntity.builder()
                .amount(fixedTermDeposit.getAmount())
                .closingDate(fixedTermDeposit.getClosingDate())
                .interests(fixedTermDeposit.getInterests())
                .account(accountEntity)
                .user(userEntity)
               .build();
        return fixedTermDepositRepository.saveAndFlush(fixedTermDepositEntity);
    }
}
