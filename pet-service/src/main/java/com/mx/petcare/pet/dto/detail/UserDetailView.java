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
 * Application: pet-service
 * Author: Jacito
 * Creation Date: 28/05/2025
 */

package com.mx.petcare.pet.dto.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detailed user profile information")
public class UserDetailView {

    @Schema(description = "User ID", example = "1001")
    private Long id;

    @Schema(description = "Full name of the user", example = "Jacinto Pérez")
    private String fullName;

    @Schema(description = "User email address", example = "jacinto@example.com")
    private String email;

    @Schema(description = "User phone number", example = "+52 123 456 7890")
    private String phoneNumber;

    @Schema(description = "User address", example = "Av. Siempre Viva 742, CDMX")
    private String address;
}

