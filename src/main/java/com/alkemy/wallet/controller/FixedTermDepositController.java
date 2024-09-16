package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDto;
import com.alkemy.wallet.exception.FixedTermDepositException;
import com.alkemy.wallet.service.FixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fixedDeposit")
public class FixedTermDepositController {
    @Autowired
    private final FixedTermDepositService fixedTermDepositService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER_ROLE')")
    public  ResponseEntity< FixedTermDepositDto > createFixedTermDeposit(@RequestBody FixedTermDepositDto fixedTermDepositDto, @RequestHeader("Authorization") String token) throws FixedTermDepositException {
        fixedTermDepositDto.setClosingDate(new Timestamp(fixedTermDepositDto.getClosingDate().getTime()+86400000));
        return ResponseEntity.ok(fixedTermDepositService.createFixedTermDeposit(fixedTermDepositDto, token));

    }


    @GetMapping("/simulate")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<FixedTermDepositSimulateDto> simulateFixedTermDeposit(@RequestBody FixedTermDepositDto fixedTermDepositDto){
        fixedTermDepositDto.setClosingDate(new Timestamp(fixedTermDepositDto.getClosingDate().getTime()+86400000));
        return ResponseEntity.ok(fixedTermDepositService.simulateFixedTermDepositDto(fixedTermDepositDto));
    }






}
