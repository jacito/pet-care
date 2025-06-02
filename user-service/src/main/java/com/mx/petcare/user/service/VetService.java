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

package com.mx.petcare.user.service;

import com.mx.petcare.user.dto.veterinarian.RegisterVetRequest;
import com.mx.petcare.user.dto.veterinarian.VetDetailResponse;
import com.mx.petcare.user.dto.veterinarian.VetResponse;

import java.util.List;


public interface VetService {

    boolean userExists(String username, String email);

    void registerVet(RegisterVetRequest request);

    List<VetResponse> getAllVeterinarians();

    VetResponse getVeterinarianById(Long id);

    VetDetailResponse getVeterinarianDetailById(Long id);
}
