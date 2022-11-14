package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDto {
    @NotEmpty(message = "Must declare a currency type (example: USD, EUR")
    @NotBlank(message = "Currency cannot be whitespaces")
    private String currency;
}
