package com.alkemy.wallet.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @NotEmpty(message = "First name cannot be empty")
    @NotBlank(message = "First name cannot be whitespaces")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @NotBlank(message = "Last name cannot be whitespaces")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be whitespaces")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be whitespaces")
    private String password;

    @NotNull(message = "Must specify the id for the role")
    private Long roleId;
}
