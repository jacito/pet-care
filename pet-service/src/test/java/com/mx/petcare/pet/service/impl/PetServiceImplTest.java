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

import static com.mx.petcare.pet.commons.Constants.PET_NOT_FOUND;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mx.petcare.pet.dto.*;
import com.mx.petcare.pet.dto.detail.*;
import com.mx.petcare.pet.dto.summary.*;
import com.mx.petcare.pet.entity.Pet;
import com.mx.petcare.pet.exception.PetException;
import com.mx.petcare.pet.repository.PetRepository;
import com.mx.petcare.pet.service.UserService;
import com.mx.petcare.pet.service.VetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserService userService;

    @Mock
    private VetService vetService;

    @InjectMocks
    private PetServiceImpl petService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void petExists_shouldReturnTrueIfExists() {
        String name = "Fido";
        when(petRepository.existsPetByName(name)).thenReturn(true);

        boolean exists = petService.petExists(name);

        assertTrue(exists);
        verify(petRepository).existsPetByName(name);
    }

    @Test
    void createPet_shouldSavePet() {
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Buddy");
        request.setSpecies("Dog");
        request.setBreed("Golden Retriever");
        request.setBirthDate(LocalDate.of(2020, 1, 1));
        request.setWeight(10.5);
        request.setGender("Male");
        request.setUserId(1L);

        petService.createPet(request);

        ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository).save(petCaptor.capture());

        Pet savedPet = petCaptor.getValue();
        assertEquals("Buddy", savedPet.getName());
        assertEquals("Dog", savedPet.getSpecies());
        assertEquals("Golden Retriever", savedPet.getBreed());
        assertEquals(LocalDate.of(2020, 1, 1), savedPet.getBirthDate());
        assertEquals(10.5, savedPet.getWeight());
        assertEquals("Male", savedPet.getGender());
        assertEquals(1L, savedPet.getUserId());
    }

    @Test
    void getPetsByUserId_shouldReturnPetVetViewList() {
        Long userId = 1L;

        Pet pet1 = Pet.builder().id(1L).name("Fido").species("Dog").vetId(10L).build();
        Pet pet2 = Pet.builder().id(2L).name("Mittens").species("Cat").vetId(null).build();

        when(petRepository.findByUserId(userId)).thenReturn(List.of(pet1, pet2));
        when(vetService.getVetViewById(10L)).thenReturn(new VetView());

        List<PetVetView> result = petService.getPetsByUserId(userId);

        assertEquals(2, result.size());
        assertNotNull(result.get(0).getPet());
        assertNull(result.get(1).getVet());

        verify(petRepository).findByUserId(userId);
        verify(vetService).getVetViewById(10L);
    }

    @Test
    void getPetDetailsForUser_shouldReturnCombinedView() {
        Long petId = 1L;
        Long userId = 2L;

        PetDetailView petDetail = new PetDetailView("Buddy", "Dog", "Golden Retriever", LocalDate.of(2020, 1, 1), 10.5, "Male");
        UserDetailView userDetail = new UserDetailView();
        userDetail.setId(userId);

        when(petRepository.findById(petId)).thenReturn(Optional.of(new Pet()));
        when(userService.getUserDetailViewById(userId)).thenReturn(userDetail);

        PetUserDetailView result = petService.getPetDetailsForUser(petId, userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserDetailView().getId());

        verify(petRepository).findById(petId);
        verify(userService).getUserDetailViewById(userId);
    }

    @Test
    void assignVeterinarian_shouldUpdateVetId() {
        Long petId = 1L;
        Long vetId = 10L;

        Pet pet = Pet.builder().id(petId).vetId(null).build();
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        petService.assignVeterinarian(petId, vetId);

        assertEquals(vetId, pet.getVetId());
        verify(petRepository).save(pet);
    }

    @Test
    void assignVeterinarian_shouldThrowExceptionWhenPetNotFound() {
        Long petId = 1L;
        Long vetId = 10L;

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        PetException ex = assertThrows(PetException.class, () -> petService.assignVeterinarian(petId, vetId));

        assertEquals(PET_NOT_FOUND, ex.getMessage());
        verify(petRepository, never()).save(any());
    }

}
