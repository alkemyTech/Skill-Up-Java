package com.alkemy.wallet.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponseDto {
    private String email;
    private String token;
}
