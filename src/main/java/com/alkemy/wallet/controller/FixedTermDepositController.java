package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
import com.alkemy.wallet.exception.FixedTermException;
import com.alkemy.wallet.service.impl.FixedTermDepositServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fixed-deposit")
public class FixedTermDepositController {

    @Autowired
    FixedTermDepositServiceImpl fixedTermDepositService;

    @PostMapping
    public ResponseEntity<FixedTermDepositResponseDTO> createFXD(@RequestHeader(value = "Authorization") String token, @RequestBody FixedTermDepositRequestDTO requestDTO) {
        return new ResponseEntity<>(fixedTermDepositService.createFXD(token, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/simulate")
    public ResponseEntity<Map<String, Object>> simulate(@RequestBody FixedTermDepositRequestDTO requestDTO) {
        return new ResponseEntity<>(fixedTermDepositService.simulate(requestDTO), HttpStatus.OK);
    }

}
