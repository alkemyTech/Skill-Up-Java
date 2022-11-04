package com.alkemy.wallet.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDto {
    private Long roleId;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
