package com.epam.esm.entity;

public enum Permission {
    MAKE_ORDER("order:create"),
    ALL_READ("all:read"),
    ALL_WRITE("all:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
