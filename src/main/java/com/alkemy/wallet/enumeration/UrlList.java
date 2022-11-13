package com.alkemy.wallet.enumeration;

public enum UrlList {

    MAIN_PATH("http://localhost:9090/");

    private String UrlString;

    UrlList(String message){
        this.UrlString= message;
    }

    public String getUrlString(){return UrlString;}


}