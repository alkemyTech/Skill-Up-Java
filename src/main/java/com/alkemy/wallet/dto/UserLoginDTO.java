package com.alkemy.wallet.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class UserLoginDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
