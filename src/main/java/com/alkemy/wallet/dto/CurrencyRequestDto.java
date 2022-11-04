package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;

public class CurrencyRequestDto {
    public String currency;

    public Currency currencyRequestToEnum(){
        return Currency.valueOf(currency);
    }
}
