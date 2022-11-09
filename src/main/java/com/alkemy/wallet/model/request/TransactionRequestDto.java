package com.alkemy.wallet.model.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDto {
    private Double amount;
    private String type;
    private String description;
    private Long userId;
    private Long accountId;
}
