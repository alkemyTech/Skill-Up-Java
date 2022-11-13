package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.ApiErrorDTO;
import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
import com.alkemy.wallet.exception.FixedTermException;
import com.alkemy.wallet.service.impl.FixedTermDepositServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fixed-deposit")
public class FixedTermDepositController {

    @Autowired
    FixedTermDepositServiceImpl fixedTermDepositService;

    //Documentation
    @Operation(
            summary = "Create a new fixed term deposit",
            description = "<h3>Endpoint that create a new fixed term deposit</h3>" +
                    "<b>Requirements to create a fixed term:</B>" +
                    "<p>- Sufficient balance</p>" +
                    "<p>- 30 days minimum investment</p>" +
                    "<p><b>Note:</b> There is no minimum amount but the transaction limits are necessary to create the fixed term as a transaction</p>"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Fixed term created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FixedTermDepositResponseDTO.class))
            }),
            @ApiResponse(responseCode = "401", description = "Not authorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            })
    })
    @PostMapping
    public ResponseEntity<FixedTermDepositResponseDTO> createFXD(@RequestHeader(value = "Authorization") String token, @RequestBody FixedTermDepositRequestDTO requestDTO) {
        return new ResponseEntity<>(fixedTermDepositService.createFXD(token, requestDTO), HttpStatus.CREATED);
    }

    //Documentation
    @Operation(
            summary = "Simulate a fixed term deposit",
            description = "<h3>Endpoint that simulate a new fixed term deposit</h3>" +
                    "<b>Requirements to simulate a fixed term:</B>" +
                    "<p>- Nothing</p>"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Fixed term simulated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FixedTermDepositResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))
            })
    })
    @GetMapping("/simulate")
    public ResponseEntity<Map<String, Object>> simulate(@RequestBody FixedTermDepositRequestDTO requestDTO) {
        return new ResponseEntity<>(fixedTermDepositService.simulate(requestDTO), HttpStatus.OK);
    }

}
