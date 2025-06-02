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

package com.mx.petcare.pet.service.impl;

import com.mx.petcare.pet.dto.*;
import com.mx.petcare.pet.dto.detail.*;
import com.mx.petcare.pet.dto.summary.*;
import com.mx.petcare.pet.entity.Pet;
import com.mx.petcare.pet.exception.PetException;
import com.mx.petcare.pet.repository.PetRepository;
import com.mx.petcare.pet.service.PetService;
import com.mx.petcare.pet.service.UserService;
import com.mx.petcare.pet.service.VetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mx.petcare.pet.commons.Constants.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;
    private final UserService userService;
    private final VetService vetService;


    @Override
    public boolean petExists(String name) {
        return petRepository.existsPetByName(name);
    }

    @Override
    public void createPet(CreatePetRequest request) {
        logger.debug("Create Pet :: {}", request);
        Pet pet = Pet.builder()
                .name(request.getName())
                .species(request.getSpecies())
                .breed(request.getBreed())
                .birthDate(request.getBirthDate())
                .weight(request.getWeight())
                .gender(request.getGender())
                .userId(request.getUserId())
                .build();

         petRepository.save(pet);
    }

    @Override
    public List<PetVetView> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId).stream()
                .map(pet -> {
                    VetView vetView = null;
                    if (pet.getVetId() != null) {
                        vetView = vetService.getVetViewById(pet.getVetId());
                    }

                    return new PetVetView(
                            new PetView(pet.getId(), pet.getName(), pet.getSpecies()), vetView);
                }).toList();
    }

    @Override
    public PetUserDetailView getPetDetailsForUser(Long petId, Long userId) {
        PetDetailView petUserDetailView = getPetDetailsById(petId);
        UserDetailView userDetailView = userService.getUserDetailViewById(userId);
        userDetailView.setId(userId);

        return new PetUserDetailView(petUserDetailView, userDetailView);
    }

    @Override
    public List<PetUserView> getPetsByVetId(Long vetId) {
        return petRepository.findByVetId(vetId).stream()
                .map(pet -> {
                    UserView userView = userService.getUserViewById(pet.getUserId());

                    return new PetUserView(
                            new PetView(pet.getId(), pet.getName(), pet.getSpecies()), userView);
                }).toList();
    }

    @Override
    public PetVetDetailView getPetDetailsForVet(Long petId, Long vetId) {
        PetDetailView petUserDetailView = getPetDetailsById(petId);
        VetDetailView vetDetailView = vetService.getVetDetailViewById(vetId);
        vetDetailView.setId(vetId);
        return new PetVetDetailView(petUserDetailView, vetDetailView);
    }

    @Override
    public void assignVeterinarian(Long petId, Long vetId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PET_NOT_FOUND));

        pet.setVetId(vetId);
        petRepository.save(pet);
    }


    private PetDetailView getPetDetailsById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PET_NOT_FOUND));

        return new PetDetailView(
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getWeight(),
                pet.getGender()
        );
    }

}

