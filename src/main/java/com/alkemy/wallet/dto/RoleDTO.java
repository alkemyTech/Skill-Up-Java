package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorRole;
import com.alkemy.wallet.model.RoleEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class RoleDTO {
    private Long id;
    @NotNull(groups = IValidatorRole.class)
    private RoleEnum name;
    @NotBlank(groups = IValidatorRole.class)
    private String description;
}
