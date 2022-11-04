package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.exception.FixedTermDepositException;
import com.alkemy.wallet.service.FixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fixedDeposit")
public class FixedTermDepositController {
    @Autowired
    private final FixedTermDepositService fixedTermDepositService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<String> createFixedTermDeposit(@Validated @RequestBody FixedTermDepositDto fixedTermDepositDto) throws FixedTermDepositException {
        fixedTermDepositDto.setClosingDate(new Timestamp(fixedTermDepositDto.getClosingDate().getTime()+86400000));
        fixedTermDepositService.createFixedTermDeposit(fixedTermDepositDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successful creation of an FixedTermDeposit.");

    }







}
