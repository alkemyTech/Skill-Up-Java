package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.request.FixedTermDepositSimulateRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositSimulationResponseDto;
import com.alkemy.wallet.service.IFixedTermDepositService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<FixedTermDepositResponseDto> createFixedTem(
            @Validated @RequestBody FixedTermDepositRequestDto requestDto,
            @RequestHeader("Authorization") String  token) {
        return ResponseEntity.status(CREATED).body(service.create(requestDto, token));
    }

    @GetMapping("/simulate")
    public ResponseEntity<FixedTermDepositSimulationResponseDto> simulateDeposit(
            @Validated @RequestBody FixedTermDepositSimulateRequestDto request) {
        return ResponseEntity.status(OK).body(service.simulateDeposit(request));
    }
}
