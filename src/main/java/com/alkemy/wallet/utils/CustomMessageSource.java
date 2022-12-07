package com.alkemy.wallet.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static java.util.Locale.US;

@Component
@RequiredArgsConstructor
public class CustomMessageSource {

    private final MessageSource messageSource;

    public String message(String code, Object[] args) {
        return messageSource.getMessage(code, args, US);
    }
}
