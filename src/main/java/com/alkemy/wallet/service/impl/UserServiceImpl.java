package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.RoleDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.dto.UserResponseDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.exception.MessageErrorEnum;
import com.alkemy.wallet.model.RoleEnum;
import com.alkemy.wallet.model.TransactionLimitEnum;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.alkemy.wallet.model.RoleEnum.ADMIN;
import static com.alkemy.wallet.model.RoleEnum.USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final BankDAO bankDAO;
    private static final Map<String, Long> ROLES = new HashMap<>();

    static {
        ROLES.put(USER.getName(), USER.getId());
        ROLES.put(ADMIN.getName(), ADMIN.getId());
    }

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO request) {

        Optional<UserEntity> userExists = Optional.ofNullable(bankDAO.getUserByEmail(request.getEmail()));

        if (userExists.isPresent()) {
            throw new BankException(MessageErrorEnum.USER_EXISTS.getMessage());
        }

        if (!request.getRoleId().equalsIgnoreCase(USER.getName()) && !request.getRoleId().equalsIgnoreCase(ADMIN.getName())) {
            throw new BankException(MessageErrorEnum.INVALID_ROLE.getMessage());
        }

        Long roleId = ROLES.get(request.getRoleId().toUpperCase());
        String roleName = request.getRoleId().equalsIgnoreCase(ADMIN.getName()) ? ADMIN.getName() : USER.getName();
        RoleEnum roleEnum = request.getRoleId().equalsIgnoreCase(ADMIN.getName()) ? ADMIN : USER;

        RoleDTO role = RoleDTO.builder()
                .id(roleId)
                .name(roleEnum)
                .description(roleName)
                .build();

        UserEntity userEntity = bankDAO.createUser(request, role);

        for (TransactionLimitEnum transactionLimitEnum: TransactionLimitEnum.values()) {
            AccountDTO accountDTO = AccountDTO.builder()
                    .currency(transactionLimitEnum.getCurrency())
                    .transactionLimit(transactionLimitEnum.getAmount())
                    .build();
            bankDAO.createAccount(accountDTO, userEntity);
        }

        UserResponseDTO response = UserResponseDTO.builder()
                .user(userEntity.getEmail())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteUser(Long userId) {
        bankDAO.deleteByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<UserEntity> showAllUsers() {
        return bankDAO.getAllUsers();
    }

    @Override
    public Optional<UserEntity> findUserById(Long userId) {
        return bankDAO.getUserById(userId);
    }

    @Override
    public ResponseEntity<Set<AccountEntity>> showAllAccountsByUserId(Long userId) {
        Optional<UserEntity> opUser = bankDAO.getUserById(userId);

        if(opUser.isEmpty()) {
            throw new BankException("The requested user ID does not exist");
        }

        return ResponseEntity.ok(opUser.get().getAccount());
    }
}
