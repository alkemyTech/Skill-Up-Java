package com.alkemy.wallet.utils;

public class FixedTermDepositUtil {

    public static final int MIN_TO_INVEST = 100;

    protected static final double INTEREST_RATE = 0.05;

    public static Double calculateInterest(Double amount, Long days) {
        double interest = 0;
        for (int i = 0; i < days; i++) {
            interest = interest + amount * INTEREST_RATE;
        }
        return interest;
    }
}
