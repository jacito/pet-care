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
 * Creation Date: 28/05/2025
 */

package com.mx.petcare.user.commons;

import com.mx.petcare.user.entity.UserProfile;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    /**
     * Constructs the full name of a user from the available name components.
     * <p>
     * This method concatenates the user's first name, middle name, last name, and second last name,
     * skipping any {@code null} values and separating each component with a space.
     * </p>
     *
     * @param profile the {@link UserProfile} object containing the user's name components
     * @return a {@link String} representing the user's full name
     */
    public static String loadFullName(UserProfile profile) {
        String fullName = Stream.of(
                profile.getFirstName(),
                profile.getMiddleName(),
                profile.getLastName(),
                profile.getSecondLastName()
        ).filter(Objects::nonNull).collect(Collectors.joining(" "));

        return fullName;
    }
}
