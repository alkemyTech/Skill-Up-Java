package com.alkemy.wallet.util;

import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean emailRegexMatches(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
    }
}
