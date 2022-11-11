package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedAccountsDto {
    @JsonProperty( "accounts" )
    private List<AccountDto> accounts;

    @JsonProperty( "previusPage" )
    private String previusPage;

    @JsonProperty( "nextPage" )
    private String nextPage;
}
