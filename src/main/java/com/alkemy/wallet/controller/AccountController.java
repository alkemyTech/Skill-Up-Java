package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.exception.AccountAlreadyExistsException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@ApiModel("Controlador de Cuentas")
public class AccountController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountDto>> getAllContinentsController(@PathVariable Long userId) throws EmptyResultDataAccessException {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId).stream()
                .map(account -> mapper.getMapper().map(account, AccountDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @PostMapping("/")
    public ResponseEntity<?> postAccount(@RequestHeader(name = "Authorization") String token, @RequestBody AccountDto accountDto) {
        return accountService.postAccount(accountDto, token);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAccountController(@PathVariable Long id, @Valid @RequestBody AccountUpdateDto newTransactionLimit, @RequestHeader("Authorization") String token) {
        return accountService.updateAccount(id, newTransactionLimit, token);
    }

}
