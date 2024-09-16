package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountPatchDto(
    @JsonProperty( "transactionLimit" )
    Double transactionLimit){
}
