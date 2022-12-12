/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alkemy.wallet.exception;

/**
 *
 * @author marti
 */
public class UserNotFound extends RuntimeException {

    public UserNotFound() {
        super();
    }

    public UserNotFound(final String message) {
        super(message);
    }

    public UserNotFound(final String message, final Throwable cause) {
        super(message, cause);
    }

}
