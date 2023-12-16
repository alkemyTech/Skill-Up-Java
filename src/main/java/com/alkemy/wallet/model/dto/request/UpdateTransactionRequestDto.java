package com.alkemy.wallet.model.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateTransactionRequestDto {

    private String description;
}
