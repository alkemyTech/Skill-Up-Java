package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDto;
import com.alkemy.wallet.dto.SimulatedFixedTermDto;
import com.alkemy.wallet.service.interfaces.IFixedTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class FixedTermDepositController {

    @Autowired
    private IFixedTermService fixedTermService;

    @PostMapping("/fixedDeposit")
    public ResponseEntity<FixedTermDto> createFixedDeposit (@Valid @RequestBody FixedTermDto fixedTermDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermService.createFixedTerm(fixedTermDto));
    }

    @PostMapping("/fixedTermDeposit/simulate")
    public ResponseEntity<SimulatedFixedTermDto> simulateFixedDeposit (@Valid @RequestBody SimulatedFixedTermDto fixedTermDto) {
        return ResponseEntity.status(HttpStatus.OK).body(fixedTermService.simulateFixedTerm(fixedTermDto));
    }

}
