package com.senla.training_2019.smolka.model.enums;

public enum UserRole {
    USER, MODERATOR, ADMIN;

    public String getStringValWithRolePrefix() {
        return "ROLE_"+toString();
    }
}
