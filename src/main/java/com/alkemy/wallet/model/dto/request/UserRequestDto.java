package com.alkemy.wallet.model.dto.request;

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

    @NotEmpty(message = "{user.empty-first-name}")
    @NotBlank(message = "{user.blank-first-name}")
    private String firstName;

    @NotEmpty(message = "{user.empty-last-name}")
    @NotBlank(message = "{user.blank-last-name}")
    private String lastName;

    @Email(message = "{user.invalid-email}")
    @NotEmpty(message = "{user.empty-email}")
    @NotBlank(message = "{user.blank-email}")
    private String email;

    @NotEmpty(message = "{user.empty-password}")
    @NotBlank(message = "{user.blank-password}")
    private String password;

    @NotNull(message = "{user.role-id}")
    private Long roleId;
}
