package com.alkemy.wallet.model.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequestDto {

    private String firstName;
    private String lastName;
    private String password;
}
