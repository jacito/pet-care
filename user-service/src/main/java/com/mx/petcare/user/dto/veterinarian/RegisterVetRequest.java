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

import com.mx.petcare.user.dto.user.RegisterUserRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request body for registering a veterinarian, including user credentials, personal and professional veterinary information.")
public class RegisterVetRequest extends RegisterUserRequest {

    @Schema(description = "Professional license number of the veterinarian.", example = "VET-123456", required = true)
    private String licenseNumber;

    @Schema(description = "Official title or degree held by the veterinarian.", example = "MVZ (Médico Veterinario Zootecnista)", required = true)
    private String professionalTitle;

    @Schema(description = "Name of the educational institution where the veterinarian studied.", example = "UNAM - Facultad de Medicina Veterinaria y Zootecnia", required = true)
    private String institution;

    @Schema(description = "Veterinarian's area of specialization.", example = "Medicina felina", required = true)
    private String specialty;
}
