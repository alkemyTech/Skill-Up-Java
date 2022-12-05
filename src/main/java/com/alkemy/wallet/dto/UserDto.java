package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role; // Clave foranea hacia ID de Role

    private Date creationDate;

    private Date updateDate;

}
