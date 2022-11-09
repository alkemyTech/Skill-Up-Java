package com.alkemy.wallet.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthRequestDto {

    @NotEmpty(message = "Must provide an email")
    @NotBlank(message = "The email cannot be whitespaces")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "The password cannot be empty")
    @NotBlank(message = "The password cannot be whitespaces")
    @NotBlank
    private String password;
}
