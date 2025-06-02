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
 */

package com.mx.petcare.user.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Contains the full profile information of the user, including personal details and contact information.")
public class UserProfileResponse {

    @Schema(description = "Full name of the user, typically in the format 'First Last'.", example = "John Doe")
    private String nameFull;

    @Schema(description = "Email address of the user.", example = "johndoe@example.com")
    private String email;
}

