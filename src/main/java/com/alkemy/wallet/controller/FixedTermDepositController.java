package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.request.FixedTermDepositSimulateRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositSimulationResponseDto;
import com.alkemy.wallet.service.IFixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/fixed-term-deposit")
public class FixedTermDepositController {
    private final IFixedTermDepositService service;

    @PostMapping
    public ResponseEntity<FixedTermDepositResponseDto> createFixedTem(@RequestBody FixedTermDepositRequestDto requestDto, @RequestHeader("Authorization") String  token) {
        return new ResponseEntity<>(service.create(requestDto, token), HttpStatus.OK);
    }

    @GetMapping("/simulate")
    public ResponseEntity<FixedTermDepositSimulationResponseDto> simulateDeposit(@Validated @RequestBody FixedTermDepositSimulateRequestDto request) {
        return ResponseEntity.status(OK).body(service.simulateDeposit(request));
    }
}
