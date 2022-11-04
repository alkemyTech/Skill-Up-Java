package com.alkemy.wallet.dto;

import lombok.*;

@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private String message;
}
