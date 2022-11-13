package com.alkemy.wallet.enumeration;

public enum ErrorList {

    TRANSACTION_LIMIT("You have exceeded the transaction limit"),
    REQUEST_FAILED("The request was not completed"),
    INSUFFICIENT_BALANCE("You need more balance for this payment"),
    OBJECT_NOT_FOUND("The requested object was not found"),
    NOT_MATCHING_IDS("User id is not matching with the specified one in the endpoint"),
    MINIMUN_DAYS_FXD("Please select amount of days greater than 30"),
    ACCOUNTS_DIFERENT_CURRENCY("The transactions between accounts has diferent currency"),
    SAME_ACCOUNT("The account you select is the same account you owned"),
    PAGE_ERROR("The page you request not found, try page 1 or go to previous page")
    ;


    private String errorMessage;

    ErrorList(String message){
        this.errorMessage= message;
    }

    public String getMessage(){return errorMessage;}


}
