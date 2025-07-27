package com.giggi.basesetup.entity;

import lombok.Getter;

@Getter
public enum RoleName {
    ROLE_USER("ROLE_USER", "Standard user role"),
    ROLE_ADMIN("ROLE_ADMIN", "Administrator role"),
    ROLE_MODERATOR("ROLE_MODERATOR", "Moderator role");

    private final String code;
    private final String description;

    RoleName(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
