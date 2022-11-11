package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(groups = IDTOValidate.class)
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String roleId;

}
