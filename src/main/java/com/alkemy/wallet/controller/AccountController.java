package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.ApiErrorDTO;
import com.alkemy.wallet.dto.AccountPageDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private IAccountService accountService;

    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Create account", description = "<h3>Endpoint that create an account for a user that is already logged in</h3>")
    @Parameter(name = "currency", description = "Currency of the account you are trying to create allows 'ARS' or 'USD' options", example = "ARS", in = ParameterIn.QUERY)
    @Parameter(name = "Authorization", hidden = true)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account created succesfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))}),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false) String currency) throws Exception {

        AccountDTO accountDTO = accountService.createAccountWithToken(token, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }


    //Documentation--------------------------------
    @Operation(security = {@SecurityRequirement(name = "Bearer")},
            summary = "Create account", description = "<h3>Get accounts by an user Id</h3>")
    @Parameter(name = "id", description = "Id of the user you want to see the accounts details", example = "1", in = ParameterIn.QUERY)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User accounts found succesfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))}),
            @ApiResponse(responseCode="400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User id doesn't exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<List<AccountDTO>> getAccountsByUser(@PathVariable Integer id) {
        List<AccountDTO> accounts = accountService.getAccountsByUser(id);
        return ResponseEntity.ok().body(accounts);
    }
    @GetMapping("/pages")
    public ResponseEntity<AccountPageDTO> getUsers(@RequestParam(value = "page"  , required = false) Integer page){
        AccountPageDTO accounts =  accountService.getAccountsByPage(page);
        return ResponseEntity.ok().body(accounts);
    }
}