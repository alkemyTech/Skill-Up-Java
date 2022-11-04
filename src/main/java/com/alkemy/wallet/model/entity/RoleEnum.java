package com.alkemy.wallet.model.entity;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("ADMIN", 1),
    USER("USER",2);

    private String name;
    private int id;

    RoleEnum(String name, int id) {
        this.name = name;
        this.id = id;
    }
}