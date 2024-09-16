package com.alkemy.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class FixedTermDepositException extends  RuntimeException{
    public FixedTermDepositException(){
        super();
    }

    public String getMessage(){
        return "the fixed plan entered, it has less than 30 days";
    }

}