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
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Contains detailed information about a veterinarian, including personal and professional details.")
public class VetDetailResponse {

    @Schema(description = "Unique identifier of the veterinarian.", example = "1")
    private long idVet;

    @Schema(description = "Full name of the veterinarian.", example = "Dr. John Doe")
    private String fullName;

    @Schema(description = "Email address of the veterinarian.", example = "dr.johndoe@example.com")
    private String email;

    @Schema(description = "Phone number of the veterinarian.", example = "+1-234-567-890")
    private String phoneNumber;

    @Schema(description = "Physical address of the veterinarian.", example = "1234 Pet Street, Cityville, TX")
    private String address;

    @Schema(description = "License number of the veterinarian.", example = "VET123456")
    private String licenseNumber;

    @Schema(description = "Professional title of the veterinarian.", example = "Doctor of Veterinary Medicine")
    private String professionalTitle;

    @Schema(description = "Institution where the veterinarian graduated or is associated with.", example = "Veterinary University of Texas")
    private String institution;

    @Schema(description = "Specialty of the veterinarian.", example = "Surgery")
    private String specialty;

}
