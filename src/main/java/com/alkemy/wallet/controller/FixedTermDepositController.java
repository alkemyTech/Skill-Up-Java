package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDto;
import com.alkemy.wallet.exception.FixedTermDepositException;
import com.alkemy.wallet.service.FixedTermDepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/fixedDeposit")
public class FixedTermDepositController {
    @Autowired
    private final FixedTermDepositService fixedTermDepositService;

    //Swagger Notation createFixedTermDeposit
    @Operation(summary = "Create FixedTermDeposit for logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FixedTermDeposit created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedTermDepositDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "The data entered does not correspond or data is missing",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly"))
                    }),
            @ApiResponse(responseCode = "500", description = "you are not logged in or there was a server error",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "User already has an account for that currency or server couldn't complete the action"))
                    })

    })
    //End Swagger notation
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER_ROLE')")
    public  ResponseEntity< FixedTermDepositDto > createFixedTermDeposit(@RequestBody FixedTermDepositDto fixedTermDepositDto, @RequestHeader("Authorization") String token) throws FixedTermDepositException {
        return ResponseEntity.ok(fixedTermDepositService.createFixedTermDeposit(fixedTermDepositDto, token));
    }



    //Swagger Notation simulateFixedTermDeposit
    @Operation(summary = "Simulate FixedTermDeposit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FixedTermDeposit simulated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedTermDepositSimulateDto.class))}),
            @ApiResponse(responseCode = "400", description = "The data entered does not correspond or data is missing",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly"))
                    }),
    })
    //End Swagger notation

    @GetMapping("/simulate")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<FixedTermDepositSimulateDto> simulateFixedTermDeposit(@RequestBody FixedTermDepositDto fixedTermDepositDto){
        return ResponseEntity.ok(fixedTermDepositService.simulateFixedTermDepositDto(fixedTermDepositDto));
    }






}
