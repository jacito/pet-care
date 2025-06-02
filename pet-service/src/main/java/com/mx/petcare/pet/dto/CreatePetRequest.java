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

package com.mx.petcare.pet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Request object for creating a new pet")
public class CreatePetRequest {

    @Schema(description = "Name of the pet", example = "Milo", required = true)
    private String name;

    @Schema(description = "Species of the pet", example = "Cat", required = true)
    private String species;

    @Schema(description = "Breed of the pet", example = "Siamese")
    private String breed;

    @Schema(description = "Birth date of the pet", example = "2022-03-10")
    private LocalDate birthDate;

    @Schema(description = "Weight of the pet in kilograms", example = "4.2")
    private Double weight;

    @Schema(description = "Gender of the pet", example = "Male")
    private String gender;

    @Schema(description = "ID of the owner (user)", example = "1001", required = true)
    private Long userId;

    @Schema(description = "ID of the assigned veterinarian (optional)", example = "2001")
    private Long vetId;
}
