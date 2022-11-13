package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.transaction.util.DepositStrategy;
import com.alkemy.wallet.service.impl.transaction.util.IncomeStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;

import com.alkemy.wallet.util.GetTokenData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
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





    @GetMapping("/{transactionId}/")
    ResponseEntity<TransactionDTO> transactionDetails(@PathVariable Integer transactionId , @RequestHeader("Authorization") String bearerToken) throws ParseException {


        String token = bearerToken.substring("Bearer ".length());
        Integer user_id = GetTokenData.getUserIdFromToken(token);
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId , user_id ));

    }

    @GetMapping("/{userId}")
    ResponseEntity<TransactionPageDTO> transactionsListByUserId(@PathVariable Integer userId , @RequestParam Integer page){


        return ResponseEntity.ok( transactionService.findAllByUserId(userId, page) );
    }




    @PostMapping("/deposit")
    ResponseEntity<?> makeDeposit(@RequestBody @Valid TransactionCreateDTO transDTO ){

     transactionService.makeTransaction(transDTO, new DepositStrategy());
     return new ResponseEntity<>( HttpStatus.CREATED);

    }


   // same as above
    @PostMapping("/payment")
    ResponseEntity<?> makePayment(@RequestBody @Valid TransactionCreateDTO transaction ){

     transactionService.makeTransaction(transaction, new PaymentStrategy());
     return new ResponseEntity<>( HttpStatus.CREATED);

    }

    @PatchMapping("/{transactionId}")
    ResponseEntity<?> transactionModification(@PathVariable Integer transactionId , @RequestBody TransactionUpdateDTO transactionUpdateDTO , @RequestHeader("Authorization") String bearerToken) throws ParseException {

        // extraigo el token del Bearer
        String token = bearerToken.substring("Bearer ".length());

        // llamo método estático
        Integer user_id = GetTokenData.getUserIdFromToken(token);

        transactionService.updateTransaction(transactionUpdateDTO,transactionId , user_id);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/sendArs")
    ResponseEntity<TransactionResponseDTO> transferMoney(@RequestBody @Valid TransactionCreateDTO transaction,
                                                         @RequestHeader(value = "Authorization") String token)throws ParseException{
        TransactionResponseDTO response = transactionService.sendMoney(transaction, token, "ARS");
        return ResponseEntity.ok().body(response);

    }
}
