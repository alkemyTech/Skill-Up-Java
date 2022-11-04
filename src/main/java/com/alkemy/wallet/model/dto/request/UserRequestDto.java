package com.alkemy.wallet.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long roleId;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private Boolean softDelete;

    private RoleRequestDto role;

    private AccountRequestDto accountRequestDto;
}
