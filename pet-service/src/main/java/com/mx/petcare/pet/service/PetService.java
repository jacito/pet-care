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

import com.mx.petcare.pet.dto.CreatePetRequest;
import com.mx.petcare.pet.dto.detail.PetUserDetailView;
import com.mx.petcare.pet.dto.detail.PetVetDetailView;
import com.mx.petcare.pet.dto.summary.PetUserView;
import com.mx.petcare.pet.dto.summary.PetVetView;
import com.mx.petcare.pet.entity.Pet;

import java.util.List;

public interface PetService {

    boolean petExists(String name);

    void createPet(CreatePetRequest request);

    List<PetVetView> getPetsByUserId(Long userId);

    PetUserDetailView getPetDetailsForUser(Long petId, Long userId);

    List<PetUserView> getPetsByVetId(Long vetId);

    PetVetDetailView getPetDetailsForVet(Long petId, Long vetId);

    void assignVeterinarian(Long petId, Long vetId);
}
