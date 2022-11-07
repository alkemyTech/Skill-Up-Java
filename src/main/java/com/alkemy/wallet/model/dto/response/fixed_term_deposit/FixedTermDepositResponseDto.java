package com.alkemy.wallet.model.dto.response.fixed_term_deposit;

import com.alkemy.wallet.model.dto.response.account.AccountResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FixedTermDepositResponseDto {
    private Long transactionsId;
    private Double amount;
    private Long accountId;
    private Double interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
}
