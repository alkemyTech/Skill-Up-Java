package com.alkemy.wallet.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> authorities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
