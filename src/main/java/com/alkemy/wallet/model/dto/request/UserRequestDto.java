package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @NotEmpty(message = "{user.invalid-first-name}")
    @NotBlank(message = "{user.invalid-first-name}")
    private String firstName;

    @NotEmpty(message = "{user.invalid-last-name}")
    @NotBlank(message = "{user.invalid-last-name}")
    private String lastName;

    @Email(message = "{user.invalid-email}")
    @NotEmpty(message = "{user.invalid-email}")
    @NotBlank(message = "{user.invalid-email}")
    private String email;

    @NotEmpty(message = "{user.invalid-password}")
    @NotBlank(message = "{user.invalid-password}")
    private String password;

    @NotEmpty(message = "{role.mismatch}")
    @NotBlank(message = "{role.mismatch}")
    private String role;
}
