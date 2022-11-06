package com.alkemy.wallet.util;

import org.springframework.stereotype.Component;

@Component
public class CalculateInterest {

    public Double getInterest(Double amount, Integer days) {
        return amount * 0.0005 * days;
    }

    public Double getInterestPlusAmount(Double amount, Integer days) {
        return amount * 0.0005 * days + amount;
    }

}
