package com.alkemy.wallet.model.dto.request;

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

    @NotEmpty(message = "{user.empty-email}")
    @NotBlank(message = "{user.blank-email}")
    @Email(message = "{user.invalid-email}")
    private String email;

    @NotEmpty(message = "{user.empty-password}")
    @NotBlank(message = "{user.blank-password}")
    private String password;
}
