package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public record UserRequestDto(
        @Getter
        @NotBlank( message = "Name cannot be empty or null" )
        @JsonProperty( "name" )
        String name,

        @Getter
        @NotBlank( message = "LastName cannot be empty or null" )
        @JsonProperty( "lastName" )
        String lastName,
        @Getter
        @NotBlank( message = "Email cannot be empty or null" )
        @JsonProperty( "email" )
        String email,
        @Getter
        @NotBlank( message = "Password cannot be empty or null" )
        @JsonProperty( "password" )
        String password
) {
}
