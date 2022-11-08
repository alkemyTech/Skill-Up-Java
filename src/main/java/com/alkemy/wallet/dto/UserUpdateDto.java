package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter@Setter
public class UserUpdateDto {
    @JsonProperty( "firstName")
    private String firstName;

    @JsonProperty( "lastName" )
    private String lastName;

    @JsonProperty("password")
    private String password;


}
