package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.AccountAlreadyExistsException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.IAccountRepository;
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

@RestController
@RequestMapping("accounts")
@ApiModel("Controlador de Cuentas")
public class AccountController {

    private JwtUtil jwtUtil;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountRepository accountRepository;

    private Mapper mapper;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountDto>> getAllContinentsController(@PathVariable Long userId) throws EmptyResultDataAccessException {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Object> postAccount(@RequestHeader(name = "Authorization") String token, @RequestBody AccountDto accountDto) {
        try {
            userService.checkLoggedUser(token);
            Long user_id = Long.parseLong(jwtUtil.getKey(token));
            accountService.checkAccountExistence(user_id, accountDto.getCurrency());
            return ResponseEntity.status(HttpStatus.OK).body(accountRepository.save(mapper.getMapper().map(accountDto, Account.class)));

        } catch (UserNotLoggedException | AccountAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccountController(@PathVariable Long id, @Valid @RequestBody AccountUpdateDto newTransactionLimit) {
        AccountDto account = accountService.updateAccount(id, newTransactionLimit);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

}
