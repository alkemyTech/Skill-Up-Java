package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(groups = IValidatorUser.class)
    private String firstName;
    @NotBlank(groups = IValidatorUser.class)
    private String lastName;
    @NotBlank(groups = IValidatorUser.class)
    private String email;
    @NotBlank(groups = IValidatorUser.class)
    private String password;
    private String roleId;

}
