package com.alkemy.wallet.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class UserResponseDTO {
    private String user;
}
