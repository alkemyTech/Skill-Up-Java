package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateDTO {
    Integer id;
    String firstName;
    String lastName;
    String password;
}
