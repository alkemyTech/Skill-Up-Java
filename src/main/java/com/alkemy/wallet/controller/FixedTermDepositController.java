package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
import com.alkemy.wallet.service.impl.FixedTermDepositServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixed-deposit")
public class FixedTermDepositController {

    @Autowired
    FixedTermDepositServiceImpl fixedTermDepositService;

    @PostMapping
    public ResponseEntity<FixedTermDepositResponseDTO> createFXD(@RequestBody FixedTermDepositRequestDTO requestDTO) {
        return new ResponseEntity<>(fixedTermDepositService.createFXD(requestDTO), HttpStatus.CREATED);
    }

}
