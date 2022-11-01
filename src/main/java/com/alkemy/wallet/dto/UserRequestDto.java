package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank( message = "Name cannot be empty or null" )
        @JsonProperty( "name" )
        String name,

        @NotBlank( message = "LastName cannot be empty or null" )
        @JsonProperty( "lastName" )
        String lastName,

        @NotBlank( message = "Email cannot be empty or null" )
        @JsonProperty( "email" )
        String email,

        @NotBlank( message = "Password cannot be empty or null" )
        @JsonProperty( "password" )
        String password
) {
}
