package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Role;
import lombok.Data;

@Data
public class UserDetailsDTO {
    Integer id;
    String firstName;
    String lastName;
    String email;
    Role role;
}
