package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.service.impl.FixedTermDepositServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fixedDeposit")
public class FixedTermDepositController {
    private final FixedTermDepositServiceImpl fixedTermDepositServiceImpl;

    public FixedTermDepositController(FixedTermDepositServiceImpl fixedTermDepositServiceImpl) {
        this.fixedTermDepositServiceImpl = fixedTermDepositServiceImpl;
    }

    @PostMapping
    public ResponseEntity<FixedTermDepositResponseDto> save(@RequestBody FixedTermDepositRequestDto requestDto,
                                                            @RequestHeader String  token) {
        FixedTermDepositResponseDto response = fixedTermDepositServiceImpl.save(requestDto, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
