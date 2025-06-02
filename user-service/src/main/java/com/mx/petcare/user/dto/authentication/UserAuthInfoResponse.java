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
 */

package com.mx.petcare.user.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Contains essential authentication details for the user, including username, password hash, and role.")
public class UserAuthInfoResponse {

    @Schema(description = "Unique identifier of the user.", example = "1")
    private Long id;

    @Schema(description = "Username provided by the user during registration or login.", example = "johndoe")
    private String username;

    @Schema(description = "Encoded (hashed) password of the user, used for authentication.", example = "$2a$10$7cdeVhtdSoZQGiZKPv9Oe.NmuPe33B8s.VhNXgH/P9Ck/BG7jFj2u")
    private String encodedPassword;

    @Schema(description = "Role assigned to the user, used for role-based access control.", example = "USER")
    private String role;

}