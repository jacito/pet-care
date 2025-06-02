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

import com.mx.petcare.pet.dto.summary.UserView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detailed veterinarian profile information")
public class VetDetailView extends UserView {

    @Schema(description = "Veterinary license number", example = "VET123456")
    private String licenseNumber;

    @Schema(description = "Professional title", example = "Veterinarian Surgeon")
    private String professionalTitle;

    @Schema(description = "Institution of graduation", example = "UNAM")
    private String institution;

    @Schema(description = "Veterinary specialty", example = "Feline Medicine")
    private String specialty;
}

