package com.alkemy.wallet.listing;

import lombok.Getter;

/**
 *
 * @author marti
 */
//Ideal que el campo name sea un Enum donde esten definidos los roles, que serán ADMIN y USER"
@Getter
public enum Rol {
    ADMIN("Administrator"),
    USER("User");

    private final String name;

    private Rol(String name) {
        this.name = name;
    }

}
