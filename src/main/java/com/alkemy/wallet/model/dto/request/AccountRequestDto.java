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

    @NotEmpty(message = "{account.invalid-currency}")
    @NotBlank(message = "{account.invalid-currency}")
    private String currency;
}
