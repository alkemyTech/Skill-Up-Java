package com.alkemy.wallet.dto;

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
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
