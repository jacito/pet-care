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

package com.mx.petcare.pet.controller;

import com.mx.petcare.pet.dto.AssignVetRequest;
import com.mx.petcare.pet.dto.CreatePetRequest;
import com.mx.petcare.pet.dto.ErrorResponse;
import com.mx.petcare.pet.dto.detail.PetUserDetailView;
import com.mx.petcare.pet.dto.summary.PetVetView;
import com.mx.petcare.pet.exception.PetException;
import com.mx.petcare.pet.service.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @Test
    void createPet_successful() {
        System.out.println("TEST :: createPet_successful");
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Firulais");

        when(petService.petExists("Firulais")).thenReturn(false);

        ResponseEntity<?> response = petController.createPet(request);

                assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pet registered successfully.", response.getBody());
        verify(petService).createPet(request);

        System.out.println(response.getBody());
    }

    @Test
    void createPet_petAlreadyExists() {
        System.out.println("TEST ::  createPet_petAlreadyExists");
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Firulais");

        when(petService.petExists("Firulais")).thenReturn(true);

        ResponseEntity<?> response = petController.createPet(request);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Pet already exists.", response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getPetsByUser_success() {
        System.out.println("TEST :: getPetsByUser_successful");
        Long userId = 1L;
        List<PetVetView> pets = List.of(new PetVetView());
        when(petService.getPetsByUserId(userId)).thenReturn(pets);

        ResponseEntity<?> response = petController.getPetsByUser(userId);

                assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pets, response.getBody());

        System.out.printf(response.toString());
    }

    @Test
    void getPetDetailForUser_success() {
        System.out.println("TEST :: getPetDetailForUser_successful");
        Long petId = 10L, userId = 1L;
        PetUserDetailView detail = new PetUserDetailView();
        when(petService.getPetDetailsForUser(petId, userId)).thenReturn(detail);

        ResponseEntity<?> response = petController.getPetDetailForUser(petId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(detail, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getPetDetailForUser_petNotFound() {
        System.out.println("TEST :: getPetDetailForUser_petNotFound");
        Long petId = 10L, userId = 1L;
        when(petService.getPetDetailsForUser(petId, userId))
                .thenThrow(new PetException("Pet not found"));

        ResponseEntity<?> response = petController.getPetDetailForUser(petId, userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("Pet not found", error.getDetails());

        System.out.println(error.getDetails());
    }

    @Test
    void assignVet_success() {
        System.out.println("TEST :: assignVet_successful");
        AssignVetRequest request = new AssignVetRequest();
        request.setPetId(10L);
        request.setVetId(5L);

        ResponseEntity<?> response = petController.assignVet(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Veterinarian assigned successfully", response.getBody());
        verify(petService).assignVeterinarian(10L, 5L);

        System.out.println(response.getBody());
    }



}
