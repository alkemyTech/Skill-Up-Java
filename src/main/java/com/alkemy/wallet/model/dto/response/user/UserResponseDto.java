package com.alkemy.wallet.model.dto.response.user;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long roleId;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean softDelete;
}
