package com.alkemy.wallet.listing;

import lombok.Getter;

/**
 *
 * @author marti
 */
//Ideal que el campo name sea un Enum donde esten definidos los roles, que serán ADMIN y USER"
@Getter
public enum RoleName {
    ROLE_ADMIN("Administrator"),
    ROLE_USER("User");

    private final String name;

    private RoleName(String name) {
        this.name = name;
    }

}
