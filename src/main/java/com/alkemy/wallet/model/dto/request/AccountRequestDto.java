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

    @NotEmpty(message = "{account.empty-currency}")
    @NotBlank(message = "{account.blank-currency}")
    private String currency;
}
