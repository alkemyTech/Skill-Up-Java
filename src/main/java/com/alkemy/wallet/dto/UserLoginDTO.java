package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorAuthenticationReq;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Builder(toBuilder = true)
@Getter
@Setter
public class UserLoginDTO {
    @NotBlank(groups= IValidatorAuthenticationReq.class)
    private String userName;
    @NotBlank(groups= IValidatorAuthenticationReq.class)
    private String password;
}
