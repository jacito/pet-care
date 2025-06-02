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

package com.mx.petcare.pet.service;

import com.mx.petcare.pet.dto.detail.VetDetailView;
import com.mx.petcare.pet.dto.summary.VetView;

public interface VetService {
    VetView getVetViewById(Long id);

    VetDetailView getVetDetailViewById(Long id);
}
