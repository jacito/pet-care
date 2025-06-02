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

package com.mx.petcare.pet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.mx.petcare.pet.commons.Constants.ERROR;

@Data
@AllArgsConstructor
@Schema(description = "Contains details about an error response, including the error code, message, and additional details.")
public class ErrorResponse {

    @Schema(description = "HTTP status code indicating the error.", example = "500")
    private int code;

    @Schema(description = "Error message describing the problem.", example = ERROR)
    private String message;

    @Schema(description = "Additional details about the error, if available.", example = "You have an error in your SQL syntax; check the manual. SQLState: 42000")
    private String details;

}
