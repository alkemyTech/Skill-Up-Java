package com.alkemy.wallet.model;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("ADMIN", 1L),
    USER("USER",2L);

    private String name;
    private Long id;

    RoleEnum(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}