package com.alkemy.wallet.controller;



import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    //Swagger Notation getTransactionDetailById
    @Operation(summary = "Get Transaction detail by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the transaction",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDetailDto.class)) })
    })
    //end Swagger notation

    @GetMapping(value = "/detail/{id}")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<TransactionDetailDto> getTransactionDetailById(@Parameter(description = "id of transaction to be searched") @PathVariable("id") Integer transactionId,@Parameter(description = "user token") @RequestHeader("Authorization") String userToken ) throws Exception {
        return ResponseEntity.ok(transactionService.getTransactionDetailById(transactionId, userToken));
    }
    //Swagger Notation createDeposit
    @Operation(summary = "Create deposit for specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "deposit succesfull",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDepositDto.class)) })
    })
    //end Swagger notation
    @PostMapping( value = "/deposit" )
    public ResponseEntity<TransactionDepositDto> createDeposit(@RequestBody TransactionDepositRequestDto transactionDepositRequestDto, @RequestHeader("Authorization") String userToken) {
        return ResponseEntity.ok(transactionService.createDeposit(transactionDepositRequestDto, userToken));
    }

    //Swagger Notation createPayment
    @Operation(summary = "Create payment for specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "payment succesfull",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionPaymentDto.class)) })
    })
    //end Swagger notation
    @PostMapping( value = "/payment" )
    public ResponseEntity<TransactionPaymentDto> createPayment(@RequestBody TransactionPaymentRequestDto transactionPaymentRequestDto, @RequestHeader("Authorization") String userToken) {
        return ResponseEntity.ok(transactionService.createPayment(transactionPaymentRequestDto, userToken));
    }


    //Swagger Notation ListTransactions
    @Operation(summary = "List of transactions by id user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionPaymentDto.class)) })
    })
    //end Swagger notation
    @GetMapping(value = "/all/{userId}")
//    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<List<TransactionDetailDto>> listTransactions(@Parameter(description = "id of user to be searched") @PathVariable Integer userId) {
        return ResponseEntity.ok(transactionService.getTransactions(userId));
    }


    //Swagger Notation updateTransaction
    @Operation(summary = "Update transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction update successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDetailDto.class)) })
    })
    //end Swagger notation
    @PatchMapping(value="/{id}")
    ResponseEntity<TransactionDetailDto> updateTransaction(@RequestBody TransactionPatchDto transactionPatchDto,@Parameter(description = "id account to be updated")  @PathVariable Integer id,@Parameter(description = "authentication token") @RequestHeader("Authorization") String userToken) throws Exception{
        return ResponseEntity.ok(transactionService.updateTransaction(transactionPatchDto,id,userToken));
    }

    //Swagger Notation sendArg
    @Operation(summary = "Send money in Argentine currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " transfer succesfull",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDetailDto.class)) })
    })
    //end Swagger notation
    @PostMapping( value = "/sendArs" )
    public ResponseEntity<TransactionDetailDto> sendArs(@RequestBody TransactionTransferRequestDto transactionTransferRequestDto,
                                                        @Parameter(description = "authentication token") @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(transactionService.sendArs(token, transactionTransferRequestDto));
    }


    //Swagger Notation sendUsd
    @Operation(summary = "Send money in USD currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " transfer succesfull",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDetailDto.class)) })
    })
    //end Swagger notation
    @PostMapping( value = "/sendUsd" )
    public ResponseEntity<TransactionDetailDto> sendUsd(@RequestBody TransactionTransferRequestDto transactionTransferRequestDto,
                                                        @Parameter(description = "authentication token") @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(transactionService.sendUsd(token, transactionTransferRequestDto));
    }
}
