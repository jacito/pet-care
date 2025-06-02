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

package com.mx.petcare.user.controller;

import com.mx.petcare.user.dto.ErrorResponse;
import com.mx.petcare.user.dto.veterinarian.RegisterVetRequest;
import com.mx.petcare.user.dto.veterinarian.VetDetailResponse;
import com.mx.petcare.user.dto.veterinarian.VetResponse;
import com.mx.petcare.user.exception.VeterinarianException;
import com.mx.petcare.user.service.impl.VetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VetControllerTest {

    @Mock
    private VetServiceImpl vetService;

    @InjectMocks
    private VetController vetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerVet_successful() {
        System.out.println("TEST :: registerVet_successful");
        RegisterVetRequest request = new RegisterVetRequest();
        request.setUsername("vet1");
        request.setEmail("vet1@example.com");

        when(vetService.userExists("vet1", "vet1@example.com")).thenReturn(false);

        ResponseEntity<?> response = vetController.registerVet(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Veterinarian registered successfully.", response.getBody());
        verify(vetService).registerVet(request);

        System.out.println(response.getBody());
    }

    @Test
    void registerVet_conflict() {
        System.out.println("TEST :: registerVet_conflict");
        RegisterVetRequest request = new RegisterVetRequest();
        request.setUsername("vet1");
        request.setEmail("vet1@example.com");

        when(vetService.userExists("vet1", "vet1@example.com")).thenReturn(true);

        ResponseEntity<?> response = vetController.registerVet(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username or email already exists.", response.getBody());
        verify(vetService, never()).registerVet(any());

        System.out.println(response.getBody());
    }

    @Test
    void registerVet_internalServerError() {
        System.out.println("TEST :: registerVet_internalServerError");
        RegisterVetRequest request = new RegisterVetRequest();
        request.setUsername("vet1");
        request.setEmail("vet1@example.com");

        when(vetService.userExists("vet1", "vet1@example.com")).thenReturn(false);
        doThrow(new RuntimeException("DB error")).when(vetService).registerVet(request);

        ResponseEntity<?> response = vetController.registerVet(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertTrue(error.getDetails().contains("DB error"));

        System.out.println(error.getDetails());
    }

    @Test
    void getAllVeterinarians_successful() {
        System.out.println("TEST :: getAllVeterinarians_successful");
        List<VetResponse> vets = List.of(new VetResponse());
        when(vetService.getAllVeterinarians()).thenReturn(vets);

        ResponseEntity<?> response = vetController.getAllVeterinarians();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vets, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getAllVeterinarians_internalServerError() {
        System.out.println("TEST :: getAllVeterinarians_internalServerError");
        when(vetService.getAllVeterinarians()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = vetController.getAllVeterinarians();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertTrue(error.getDetails().contains("Error"));
        System.out.println(error.getDetails());
    }

    @Test
    void getVeterinarianById_successful() {
        System.out.println("TEST :: getVeterinarianById_successful");
        Long id = 1L;
        VetResponse vet = new VetResponse();
        when(vetService.getVeterinarianById(id)).thenReturn(vet);

        ResponseEntity<?> response = vetController.getVeterinarianById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vet, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getVeterinarianById_notFound() {
        System.out.println("TEST :: getVeterinarianById_notFound");
        Long id = 1L;
        when(vetService.getVeterinarianById(id)).thenThrow(new VeterinarianException("Veterinarian not found"));

        ResponseEntity<?> response = vetController.getVeterinarianById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("Veterinarian not found", error.getDetails());
        System.out.println(error.getDetails());
    }

    @Test
    void getVeterinarianDetailById_successful() {
        System.out.println("TEST :: getVeterinarianDetailById_successful");
        Long id = 1L;
        VetDetailResponse detail = new VetDetailResponse();
        when(vetService.getVeterinarianDetailById(id)).thenReturn(detail);

        ResponseEntity<?> response = vetController.getVeterinarianDetailById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(detail, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getVeterinarianDetailById_notFound() {
        System.out.println("TEST :: getVeterinarianDetailById_notFound");
        Long id = 1L;
        when(vetService.getVeterinarianDetailById(id)).thenThrow(new VeterinarianException("Veterinarian not found"));

        ResponseEntity<?> response = vetController.getVeterinarianDetailById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("Veterinarian not found", error.getDetails());

        System.out.println(error.getDetails());
    }
}
