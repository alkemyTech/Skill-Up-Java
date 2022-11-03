package com.alkemy.wallet.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<AccountResponseDto> accountResponseDto;
}
