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
 */

package com.mx.petcare.pet.dto.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "Detailed information about a pet")
public class PetDetailView {

    @Schema(description = "Name of the pet", example = "Luna")
    private String name;

    @Schema(description = "Species of the pet", example = "Dog")
    private String species;

    @Schema(description = "Breed of the pet", example = "Golden Retriever")
    private String breed;

    @Schema(description = "Birth date of the pet", example = "2020-11-15")
    private LocalDate birthDate;

    @Schema(description = "Weight of the pet in kilograms", example = "12.5")
    private Double weight;

    @Schema(description = "Gender of the pet", example = "Female")
    private String gender;

}
