package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.response.fixed_term_deposit.FixedTermDepositResponseDto;
import com.alkemy.wallet.service.FixedTermDepositService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fixedDeposit")
public class FixedTermDepositController {
    private final FixedTermDepositService fixedTermDepositService;

    public FixedTermDepositController(FixedTermDepositService fixedTermDepositService) {
        this.fixedTermDepositService = fixedTermDepositService;
    }

    @PostMapping
    public ResponseEntity<FixedTermDepositResponseDto> save(@RequestBody FixedTermDepositRequestDto requestDto) {
        FixedTermDepositResponseDto response = fixedTermDepositService.save(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
