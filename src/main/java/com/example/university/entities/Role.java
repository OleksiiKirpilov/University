package com.example.university.entities;

/**
 * User role type.
 */

public enum Role {
    ADMIN, USER;

    public String getName() {
        return name().toLowerCase();
    }


    public static final String ADMIN_ROLE_NAME = "admin";
    public static final String USER_ROLE_NAME = "user";

    public static boolean isAdmin(String role) {
        return ADMIN_ROLE_NAME.equals(role);
    }

    public static boolean isUser(String role) {
        return USER_ROLE_NAME.equals(role);
    }

}
