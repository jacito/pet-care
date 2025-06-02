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

package com.mx.petcare.user.service.impl;

import com.mx.petcare.user.dto.veterinarian.RegisterVetRequest;
import com.mx.petcare.user.dto.veterinarian.VetDetailResponse;
import com.mx.petcare.user.dto.veterinarian.VetResponse;
import com.mx.petcare.user.entity.UserPetCare;
import com.mx.petcare.user.entity.UserProfile;
import com.mx.petcare.user.entity.VetProfile;
import com.mx.petcare.user.exception.VeterinarianException;
import com.mx.petcare.user.repository.UserProfileRepository;
import com.mx.petcare.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.mx.petcare.user.dto.Role.VET;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private VetServiceImpl vetService;

    private RegisterVetRequest registerVetRequest;

    @BeforeEach
    void setup() {
        registerVetRequest = new RegisterVetRequest();
        registerVetRequest.setUsername("drsmith");
        registerVetRequest.setPassword("secret");
        registerVetRequest.setFirstName("John");
        registerVetRequest.setMiddleName("A.");
        registerVetRequest.setLastName("Smith");
        registerVetRequest.setSecondLastName("Doe");
        registerVetRequest.setPhoneNumber("1234567890");
        registerVetRequest.setEmail("john.smith@example.com");
        registerVetRequest.setAddress("123 Vet St");
        registerVetRequest.setLicenseNumber("LIC12345");
        registerVetRequest.setProfessionalTitle("DVM");
        registerVetRequest.setInstitution("Vet University");
        registerVetRequest.setSpecialty("Surgery");
    }


    @Test
    void testRegisterVet_success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        vetService.registerVet(registerVetRequest);

        ArgumentCaptor<UserPetCare> userCaptor = ArgumentCaptor.forClass(UserPetCare.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserPetCare savedVet = userCaptor.getValue();
        assertEquals("drsmith", savedVet.getUsername());
        assertEquals("encoded-password", savedVet.getPassword());
        assertEquals(VET.getRole(), savedVet.getRole());
        assertNotNull(savedVet.getUserProfile());
        assertEquals("LIC12345", savedVet.getUserProfile().getVetProfile().getLicenseNumber());
    }

    @Test
    void testGetAllVeterinarians_returnsList() {
        UserPetCare vet1 = createVetEntity(1L, "Alice Vet", "Dermatology");
        UserPetCare vet2 = createVetEntity(2L, "Bob Vet", "Dentistry");

        when(userRepository.findByRole(VET.getRole())).thenReturn(Arrays.asList(vet1, vet2));

        List<VetResponse> vets = vetService.getAllVeterinarians();

        assertEquals(2, vets.size());
        assertEquals("Alice Vet", vets.get(0).getFullName());
        assertEquals("Dermatology", vets.get(0).getSpecialty());
        assertEquals("Bob Vet", vets.get(1).getFullName());
        assertEquals("Dentistry", vets.get(1).getSpecialty());
    }

    @Test
    void testGetVeterinarianById_found() {
        UserPetCare vet = createVetEntity(1L, "John Doe", "Cardiology");

        when(userRepository.findById(1L)).thenReturn(Optional.of(vet));

        VetResponse response = vetService.getVeterinarianById(1L);

        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getFullName());
        assertEquals("Cardiology", response.getSpecialty());
    }

    @Test
    void testGetVeterinarianById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        VeterinarianException ex = assertThrows(VeterinarianException.class, () -> {
            vetService.getVeterinarianById(99L);
        });

        assertEquals("Veterinarian not found", ex.getMessage());
    }

    @Test
    void testGetVeterinarianDetailById_found() {
        UserPetCare vet = createVetEntity(1L, "Jane Doe", "Neurology");
        vet.getUserProfile().setEmail("jane.doe@example.com");
        vet.getUserProfile().setPhoneNumber("555-1234");
        vet.getUserProfile().setAddress("456 Vet Lane");
        vet.getUserProfile().getVetProfile().setProfessionalTitle("DVM");
        vet.getUserProfile().getVetProfile().setInstitution("Vet Institute");

        when(userRepository.findById(1L)).thenReturn(Optional.of(vet));

        VetDetailResponse detail = vetService.getVeterinarianDetailById(1L);

        assertEquals(1L, detail.getIdVet());
        assertEquals("Jane Doe", detail.getFullName());
        assertEquals("jane.doe@example.com", detail.getEmail());
        assertEquals("555-1234", detail.getPhoneNumber());
        assertEquals("456 Vet Lane", detail.getAddress());
        assertEquals("LIC12345", detail.getLicenseNumber());
        assertEquals("DVM", detail.getProfessionalTitle());
        assertEquals("Vet Institute", detail.getInstitution());
        assertEquals("Neurology", detail.getSpecialty());
    }

    @Test
    void testGetVeterinarianDetailById_notFound() {
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        VeterinarianException ex = assertThrows(VeterinarianException.class, () -> {
            vetService.getVeterinarianDetailById(50L);
        });

        assertEquals("Veterinarian not found", ex.getMessage());
    }

    private UserPetCare createVetEntity(Long id, String fullName, String specialty) {
        VetProfile vetProfile = VetProfile.builder()
                .licenseNumber("LIC12345")
                .professionalTitle("DVM")
                .institution("Vet Institute")
                .specialty(specialty)
                .build();

        UserProfile profile = UserProfile.builder()
                .firstName(fullName.split(" ")[0])
                .lastName(fullName.split(" ").length > 1 ? fullName.split(" ")[1] : "")
                .vetProfile(vetProfile)
                .email(fullName.toLowerCase().replace(" ", ".") + "@example.com")
                .phoneNumber("123456789")
                .address("Vet address")
                .build();

        vetProfile.setProfile(profile);

        UserPetCare vet = UserPetCare.builder()
                .id(id)
                .username(fullName.toLowerCase().replace(" ", ""))
                .password("encodedpassword")
                .role(VET.getRole())
                .userProfile(profile)
                .build();

        profile.setUserpetCare(vet);

        return vet;
    }
}
