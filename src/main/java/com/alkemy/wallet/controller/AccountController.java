package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.AccountModelAssembler;
import com.alkemy.wallet.assembler.model.AccountModel;
import com.alkemy.wallet.assembler.model.TransactionModel;
import com.alkemy.wallet.assembler.model.UserModel;
import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.BasicAccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
//@ApiModel("Controlador de Cuentas")
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

    @Autowired
    private AccountModelAssembler accountModelAssembler;

    @Autowired
    private PagedResourcesAssembler<AccountDto> pagedResourcesAssembler;

    @GetMapping("/{userId}")
    public ResponseEntity<List<BasicAccountDto>> getAllAccountByUserId(@PathVariable Long userId) throws EmptyResultDataAccessException {
        List<BasicAccountDto> accounts = accountService.getAccountsByUserId(userId).stream()
                .map(account -> mapper.getMapper().map(account, BasicAccountDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @GetMapping
    public ResponseEntity<PagedModel<AccountModel>> getTransactionPage(@RequestParam(defaultValue = "0") int page) {

        Page<AccountDto> accounts = accountService.findAllAccountsPageable(page);

        PagedModel<AccountModel> model = pagedResourcesAssembler.toModel(accounts, accountModelAssembler);

        return ResponseEntity.ok().body(model);
    }

    @PostMapping
    public ResponseEntity<?> postAccount(@RequestHeader(name = "Authorization") String token, @RequestBody BasicAccountDto basicAccountDto) {
        return accountService.postAccount(basicAccountDto, token);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAccountController(@PathVariable Long id, @Valid @RequestBody AccountUpdateDto newTransactionLimit, @RequestHeader("Authorization") String token) {
        return accountService.updateAccount(id, newTransactionLimit, token);
    }

    @GetMapping("/balance")
    public ResponseEntity<List<BalanceDto>> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getBalance(token));
    }

}