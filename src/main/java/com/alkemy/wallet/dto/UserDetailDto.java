package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class UserDetailDto {

    @JsonProperty( "firstName")
    private String firstName;

    @JsonProperty( "lastName" )
    private String lastName;

    @JsonProperty( "email" )
    private String email;

    @JsonProperty( "creationDate" )
    private Timestamp creationDate;
}
