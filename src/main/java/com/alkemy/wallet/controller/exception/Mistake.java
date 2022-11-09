package com.alkemy.wallet.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Mistake extends RuntimeException{
    private final String message;
}
