package com.alkemy.wallet.model;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("ADMIN", new Long(1)),
    USER("USER",new Long(2));

    private String name;
    private Long id;

    RoleEnum(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}