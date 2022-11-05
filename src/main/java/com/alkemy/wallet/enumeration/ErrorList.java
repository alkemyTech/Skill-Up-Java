package com.alkemy.wallet.enumeration;

public enum ErrorList {

    TRANSACTION_LIMIT("You have exceeded the transaction limit"),
    REQUEST_FAILED("The request was not completed"),
    INSUFFICIENT_BALANCE("You need more balance for this payment"),
    OBJECT_NOT_FOUND("The requested object was not found");

    private String errorMessage;

    ErrorList(String message){
        this.errorMessage= message;
    }

    public String getMessage(){return errorMessage;}


}
