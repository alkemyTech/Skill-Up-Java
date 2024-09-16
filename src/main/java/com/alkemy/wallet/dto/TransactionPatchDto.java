package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionPatchDto (
        @JsonProperty( "description" )
        String description
){

}
