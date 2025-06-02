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

package com.mx.petcare.user.dto.veterinarian;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Contains basic information about a veterinarian, including ID, full name, and specialty.")
public class VetResponse {

    @Schema(description = "Unique identifier of the veterinarian.", example = "1")
    private Long id;

    @Schema(description = "Full name of the veterinarian.", example = "Dr. John Doe")
    private String fullName;

    @Schema(description = "Specialty of the veterinarian.", example = "Surgery")
    private String specialty;

}
