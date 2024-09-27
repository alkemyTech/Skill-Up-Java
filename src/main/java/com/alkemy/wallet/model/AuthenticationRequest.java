package com.alkemy.wallet.model;

import com.alkemy.wallet.dto.validator.IValidatorAuthenticationReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank(groups= IValidatorAuthenticationReq.class)
    private String userName;
    @NotBlank(groups= IValidatorAuthenticationReq.class)
    private String password;
}
