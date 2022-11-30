package com.alkemy.wallet.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseUserDto {

    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email(message = "the email must be real email")
    private String email;

    @NotEmpty
    private String password;
/*
 Agregar una vez que esten disponibles las entidades
*/
//    private Role role;
//    private List<AccountDto> accounts;

}
