package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.alkemy.wallet.service.FixedTermDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fixedDeposit")
public class FixedTermDepositController {
    private final FixedTermDepositService fixedTermDepositService;

    @PostMapping("/create")
    public void createFixedTermDeposit(@Validated @RequestBody FixedTermDepositDto fixedTermDepositDto){

        fixedTermDepositService.createFixedTermDeposit(fixedTermDepositDto);

    }







}
