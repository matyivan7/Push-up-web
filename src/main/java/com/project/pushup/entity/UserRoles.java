package com.project.pushup.entity;

public enum UserRoles {

    ROLE_USER("USER"),
    ROLE_GUEST("GUEST"),
    ROLE_ADMIN("ADMIN");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
