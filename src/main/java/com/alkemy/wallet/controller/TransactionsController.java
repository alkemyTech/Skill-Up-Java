package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.TransactionModelAssembler;
import com.alkemy.wallet.assembler.model.TransactionModel;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ITransactionService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@RestController
@ApiModel("Controlador de Transacciones")
public class TransactionsController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    private JwtUtil jwtUtil;

    private Mapper mapper;

    @Autowired
    private TransactionModelAssembler transactionModelAssembler;

    @Autowired
    private PagedResourcesAssembler<Transaction> pagedResourcesAssembler;


    @Autowired
    private IUserService userService;

    @GetMapping("/transactions/{userId}")
    public HashSet<TransactionDto> getTransactions(@PathVariable("userId") Long userId) {
        return transactionService.getByUserId(accountService.getAccountsByUserId(userId));
    }

    @PatchMapping("/transactions/{id}")
    public ResponseEntity<Object> patchTransaction(@PathVariable("id") Long id, @RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto transactionDto) {
        try {
            userService.checkLoggedUser(token);
            Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La transaccion no existe"));
            transaction.setDescription(transactionDto.getDescription());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.save(transaction), TransactionDto.class));
        } catch (UserNotLoggedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e);
        }
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        try {
            userService.checkLoggedUser(token);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.findById(id), TransactionDto.class));
        } catch (UserNotLoggedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/transactions/page/{id}")
    public ResponseEntity<PagedModel<TransactionModel>> getTransactionPage(@PathVariable("userId") Long userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestHeader("Authorization") String token) {
        Page<Transaction> transactions = transactionService.paginateTransactionByUserId(userId, page, size, token);

        PagedModel<TransactionModel> model = pagedResourcesAssembler.toModel(transactions, transactionModelAssembler);

        return ResponseEntity.ok().body(model);
    }

    @PostMapping("/transactions/sendUsd")
    public ResponseEntity<Object> sendUsd(@RequestHeader(name = "Authorization") String
                                                  token, @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);

    }

    @PostMapping("/transactions/sendArs")
    public ResponseEntity<Object> sendArs(@RequestHeader(name = "Authorization") String
                                                  token, @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }
}