package com.alkemy.wallet.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@Data
public class ResponseUserDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email(message = "the email must be real email")
    private String email;

    private String token;

}
