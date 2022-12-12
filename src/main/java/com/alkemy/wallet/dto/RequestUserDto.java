package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {

    @NotEmpty(message = "First name shouldn't be null or empty")
    private String firstName;

    @NotEmpty(message = "Last name shouldn't be null or empty")
    private String lastName;

    @Email(message = "Email must be a real email")
    private String email;

    @NotEmpty(message = "Password shouldn't be null or empty")
    private String password;

}
