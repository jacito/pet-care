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

package com.mx.petcare.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Detailed error message", example = "Invalid username or password.")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "401")
    private int code;

    @Schema(description = "HTTP reason phrase", example = "Unauthorized")
    private String message;

    @Schema(description = "Detailed error message", example = "Invalid username or password.")
    private String details;
}
