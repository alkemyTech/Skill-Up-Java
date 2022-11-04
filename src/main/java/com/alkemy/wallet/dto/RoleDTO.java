package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.entity.RoleEnum;
import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class RoleDTO {
    private int id;
    private RoleEnum name;
    private String description;
}
