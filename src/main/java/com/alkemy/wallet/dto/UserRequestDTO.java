package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDTO {

    @NotNull(message = "Missing Data")
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
