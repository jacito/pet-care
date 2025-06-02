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
 * Application: auth-service
 * Author: Jacito
 * Creation Date: 28/05/2025
 */

package com.mx.petcare.auth.commons;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String rawPassword = "securepassword123";
    @Test
    public void testPasswordEncoding() {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Encoded Password: " + encodedPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testPasswordValidation() {
        String encodedPassword = "$2a$10$Of5noDJASAacJ/etNPDxeem0Ex9RAHJGD5W616.2O59cexNNDienC";

        System.out.println("Encoded Password: " + encodedPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
