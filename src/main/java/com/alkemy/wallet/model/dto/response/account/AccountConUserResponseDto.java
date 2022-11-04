package com.alkemy.wallet.model.dto.response.account;

import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountConUserResponseDto {
    private Long accountId;
    private String currency;
    private Double transactionLimit;
    private Double balance;
    private Long userId;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean softDelete;
    private UserResponseDto userResponseDto;
}
