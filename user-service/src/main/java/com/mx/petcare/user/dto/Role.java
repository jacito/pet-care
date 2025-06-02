/**
 * Copyright © 2025 Jazmín Velázquez Bustos. All rights reserved.
 * <p>
 * This file and its contents are protected by copyright law.
 * Reproduction, distribution, or use of any part of this software
 * without the prior written consent of the author is strictly prohibited.
 * <p>
 * This code was developed as part of the final evaluation project
 * for the Java Microservices Bootcamp offered by Código Facilito.
 * <p>
 * Project: PetCare - Pet Health and Wellness Management System
 * Application: user-service
 * Author: Jacito
 */

package com.mx.petcare.user.dto;

public enum Role {
    USER("USER"),
    VET("VET"),
    UNKNOWN("UNKNOWN");
    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public Role getEnumRole(String role) {
        switch (role) {
            case "USER":
                return USER;
            case "VET":
                return VET;
            default:
                return UNKNOWN;
        }
    }
}
