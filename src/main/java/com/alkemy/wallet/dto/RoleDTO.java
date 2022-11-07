package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.RoleEnum;
import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class RoleDTO {
    private Long id;
    private RoleEnum name;
    private String description;
}
