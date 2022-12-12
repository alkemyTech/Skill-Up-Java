package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDto;
import com.alkemy.wallet.dto.SimulatedFixedTermDto;
import com.alkemy.wallet.service.interfaces.IFixedTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class FixedTermDepositController {

    @Autowired
    private IFixedTermService fixedTermService;

    @PostMapping("/fixedDeposit")
    @Operation(summary = "Create fixed term deposit",
            description = "Generates a fixed term deposit with a defined ammount and interest",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fixed term deposit created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FixedTermDto.class))}),
            @ApiResponse(responseCode = "400", description = "Something went wrong",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<FixedTermDto> createFixedDeposit (
            @Parameter(name = "Fixed term deposit info",
                    required = true)
            @Valid @RequestBody FixedTermDto fixedTermDto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fixedTermService.createFixedTerm(fixedTermDto, token));
    }

    @PostMapping("/fixedTermDeposit/simulate")
    @Operation(summary = "Simulate fixed term deposit",
            description = "Generates a simulation of a fixed term deposit with a defined ammount and interest",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fixed term deposit simulated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SimulatedFixedTermDto.class))}),
            @ApiResponse(responseCode = "400", description = "Something went wrong",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<SimulatedFixedTermDto> simulateFixedDeposit (
            @Parameter(name = "Simulated fixed term deposit info",
                    required = true)
            @Valid @RequestBody SimulatedFixedTermDto fixedTermDto) {
        return ResponseEntity.status(HttpStatus.OK).body(fixedTermService.simulateFixedTerm(fixedTermDto));
    }

}
