package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import com.alkemy.wallet.service.impl.transaction.util.DepositStrategy;
import com.alkemy.wallet.service.impl.transaction.util.IncomeStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;

import com.alkemy.wallet.util.GetTokenData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    ITransactionService transactionService;
    @Autowired
    AccountServiceImpl accountService;




    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Transaction Detail", description = "<h3>Endpoint that show details of a transaction required in the path.</h3>" +
            "<p>Also, take the token in header for check only user logged can see details" +
            "</br><b>Note: </b>If transaction not exist throw a error message in json format with info</p>")
    @Parameters(value = { @Parameter(name = "transactionId", description = "Transaction ID", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "Authorization", hidden= true)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Transaction", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    //Mapping---------------------------------------
    @GetMapping("/{transactionId}/")
    ResponseEntity<TransactionDTO> transactionDetails(@PathVariable Integer transactionId ,@RequestHeader("Authorization") String bearerToken) throws ParseException {

        String token = bearerToken.substring("Bearer ".length());
        Integer user_id = GetTokenData.getUserIdFromToken(token);
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId, user_id));

    }
    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Transaction List by User", description = "<h3>Endpoint that show a list of transactions that user required in the path has make.</h3>" +
            "<p>Also, ADMIN users can use this endpoint for see transactions at specific user" +
            "</br><b>Note: </b>Can switch between pages</p>")
    @Parameters(value = { @Parameter(name = "userId", description = "User ID", example = "1", in = ParameterIn.PATH)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Transactions", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    //Mapping---------------------------------------
    @GetMapping("/{userId}")
    ResponseEntity<TransactionPageDTO> transactionsListByUserId(@PathVariable Integer userId,
            @RequestParam Integer page) {

        return ResponseEntity.ok(transactionService.findAllByUserId(userId, page));
    }



    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Make Deposit", description = "<h3>Endpoint that create a deposit</h3>" +
            "<p>You send amount , account destiny id, and description" +
            "</br><b>Note: </b>Not response any body. Just create a transaction.</p>")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    //Mapping---------------------------------------
    @PostMapping("/deposit")
    ResponseEntity<?> makeDeposit(@RequestBody @Valid TransactionCreateDTO transDTO) {

        transactionService.makeTransaction(transDTO, new DepositStrategy());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Make Payment", description = "<h3>Endpoint that create a payment</h3>" +
            "<p>You send amount , account send amount id, and description" +
            "</br><b>Note: </b>Not response any body. Just create a transaction.</p>")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    //Mapping---------------------------------------
    @PostMapping("/payment")
    ResponseEntity<?> makePayment(@RequestBody @Valid TransactionCreateDTO transaction) {

        transactionService.makeTransaction(transaction, new PaymentStrategy());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Update a transaction", description = "<h3>Endpoint that update transaction description</h3>" +
            "<p>You update only description" +
            "</br><b>Note: </b>Not response any body. Just update a transaction. Check if you modify you own transaction</p>")
    @Parameters(value = { @Parameter(name = "transactionId", description = "Transaction ID", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "Authorization", hidden= true)})
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transaction Update", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    //Mapping---------------------------------------
    @PatchMapping("/{transactionId}")
    ResponseEntity<?> transactionModification(@PathVariable Integer transactionId,
            @RequestBody TransactionUpdateDTO transactionUpdateDTO, @RequestHeader("Authorization") String bearerToken)
            throws ParseException {

        // extraigo el token del Bearer
        String token = bearerToken.substring("Bearer ".length());

        // llamo método estático
        Integer user_id = GetTokenData.getUserIdFromToken(token);

        transactionService.updateTransaction(transactionUpdateDTO, transactionId, user_id);
        return ResponseEntity.ok().build();

    }

    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Make a Send ARS", description = "<h3>Endpoint that create a payment and income transaction</h3>" +
            "<p>You send amount , account destiny id, and description" +
            "</br><b>Note: </b>Not response any body. Just create a transaction.</p>")
    @Parameters(value = @Parameter(name = "Authorization", hidden= true))
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    //Mapping---------------------------------------
    @PostMapping("/sendArs")
    ResponseEntity<TransactionResponseDTO> transferMoney(@RequestBody @Valid TransactionCreateDTO transaction,
            @RequestHeader(value = "Authorization") String token) throws ParseException {
        TransactionResponseDTO response = transactionService.sendMoney(transaction, token, "ARS");
        return ResponseEntity.ok().body(response);

    }

    @Operation(security = {
            @SecurityRequirement(name = "Bearer") }, summary = "Make a Send USD", description = "<h3>Endpoint that create a payment and income transaction</h3>"
                    + "<p>You send amount , account destiny id, and description"
                    + "</br><b>Note: </b>Responds with a payment. Just create a transaction.</p>")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class)) }) })
    @PostMapping("/sendUsd")
    public ResponseEntity<?> sendUsd(@RequestBody @Valid TransactionCreateDTO transactionDTO,
            @RequestHeader(value = "Authorization") String bearerToken)
            throws ParseException {
        TransactionResponseDTO response = transactionService.sendMoney(transactionDTO, bearerToken, "USD");
        return ResponseEntity.ok().body(response);
    }
}
