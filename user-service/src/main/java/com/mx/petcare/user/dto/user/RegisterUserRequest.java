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

package com.mx.petcare.user.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Request body for registering a standard user, including credentials and personal information.")
public class RegisterUserRequest {

    @Schema(description = "Unique username for login.", example = "catlover92", required = true)
    private String username;

    @Schema(description = "Password for the account (will be encoded).", example = "SecurePass123!", required = true)
    private String password;

    @Schema(description = "First name of the user.", example = "María", required = true)
    private String firstName;

    @Schema(description = "Middle name of the user (optional).", example = "Isabel")
    private String middleName;

    @Schema(description = "First surname of the user.", example = "González", required = true)
    private String lastName;

    @Schema(description = "Second surname of the user (optional).", example = "Torres")
    private String secondLastName;

    @Schema(description = "Email address of the user.", example = "maria.gonzalez@example.com", required = true)
    private String email;

    @Schema(description = "Phone number of the user.", example = "+52-555-123-4567")
    private String phoneNumber;

    @Schema(description = "Residential address of the user.", example = "Av. Universidad 3000, CDMX")
    private String address;
}
