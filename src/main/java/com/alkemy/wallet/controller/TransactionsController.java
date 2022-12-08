package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.TransactionModelAssembler;
import com.alkemy.wallet.assembler.model.TransactionModel;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ITransactionService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private PagedResourcesAssembler<TransactionDto> pagedResourcesAssembler;

    @Autowired
    private IUserService userService;

    @GetMapping("/transactions/{userId}")
    public HashSet<TransactionDto> getTransactions(@PathVariable("userId") Long userId) {
        return transactionService.getByUserId(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        return transactionService.getTransaction(id, token);
    }

    @PatchMapping("/transactions/{id}")
    public ResponseEntity<?> patchTransaction(@PathVariable("id") Long id, @RequestHeader(name = "Authorization") String token, @RequestBody String description) {
        return transactionService.patchTransaction(id, token, description);
    }

    @GetMapping("/transactions/page/{id}")
    public ResponseEntity<PagedModel<TransactionModel>> getTransactionPage(@PathVariable("id") Long userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestHeader("Authorization") String token) {
        Page<TransactionDto> transactions = transactionService.findAllTransactionsByUserIdPageable(userId, page, size, token);

        PagedModel<TransactionModel> model = pagedResourcesAssembler.toModel(transactions, transactionModelAssembler);

        return ResponseEntity.ok().body(model);
    }

    @PostMapping("/transactions/sendUsd")
    public ResponseEntity<Object> sendUsd(@RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }

    @PostMapping("/transactions/sendArs")
    public ResponseEntity<Object> sendArs(@RequestHeader(name = "Authorization") String
                                                  token, @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }

    @PostMapping("/transactions/deposit")
    public ResponseEntity<?> postDeposit(@RequestBody TransactionDto transactionDto) {
        return transactionService.createDeposit(transactionDto);
    }

    @PostMapping("/transactions/payment")
    public ResponseEntity<?> postPayment(@RequestBody TransactionDto transcationDto) {
        return transactionService.createPayment(transcationDto);
    }
}