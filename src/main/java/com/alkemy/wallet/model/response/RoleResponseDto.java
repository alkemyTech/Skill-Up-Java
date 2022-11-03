package com.alkemy.wallet.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {
    private Long roleId;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
