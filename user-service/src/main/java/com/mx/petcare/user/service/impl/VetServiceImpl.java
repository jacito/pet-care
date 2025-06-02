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
import com.mx.petcare.user.service.VetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.mx.petcare.user.commons.Constants.VET_NOT_FOUND;
import static com.mx.petcare.user.commons.Util.loadFullName;
import static com.mx.petcare.user.dto.Role.VET;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private static final Logger logger = LoggerFactory.getLogger(VetServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean userExists(String username, String email) {
        return userRepository.existsByUsername(username) || userProfileRepository.existsByEmail(email);
    }

    @Override
    public void registerVet(RegisterVetRequest request) {
        logger.debug("Register vet :: {}", request.getUsername());

        VetProfile vetProfile = VetProfile.builder()
                .licenseNumber(request.getLicenseNumber())
                .professionalTitle(request.getProfessionalTitle())
                .institution(request.getInstitution())
                .specialty(request.getSpecialty())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .secondLastName(request.getSecondLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .vetProfile(vetProfile)
                .build();

        vetProfile.setProfile(userProfile);

        UserPetCare vet = UserPetCare.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(VET.getRole())
                .userProfile(userProfile)
                .build();

        userProfile.setUserpetCare(vet);

        userRepository.save(vet);
    }

    @Override
    public List<VetResponse> getAllVeterinarians() {
        return userRepository.findByRole(VET.getRole()).stream()
                .filter(userPetCare -> VET.getRole().equals(userPetCare.getRole()))
                .map(userPetCare -> new VetResponse(
                        userPetCare.getId(),
                        loadFullName(userPetCare.getUserProfile()),
                        userPetCare.getUserProfile().getVetProfile().getSpecialty()))
                .collect(Collectors.toList());
    }

    @Override
    public VetResponse getVeterinarianById(Long id) {
        return userRepository.findById(id)
                .filter(userPetCare -> VET.getRole().equals(userPetCare.getRole()))
                .map(userPetCare -> new VetResponse(
                        userPetCare.getId(),
                        loadFullName(userPetCare.getUserProfile()),
                        userPetCare.getUserProfile().getVetProfile().getSpecialty()))
                .orElseThrow(() -> new VeterinarianException(VET_NOT_FOUND));
    }

    @Override
    public VetDetailResponse getVeterinarianDetailById(Long id) {
        return userRepository.findById(id)
                .filter(userPetCare -> VET.getRole().equals(userPetCare.getRole()))
                .map(userPetCare -> new VetDetailResponse(
                        userPetCare.getId(),
                        loadFullName(userPetCare.getUserProfile()),
                        userPetCare.getUserProfile().getEmail(),
                        userPetCare.getUserProfile().getPhoneNumber(),
                        userPetCare.getUserProfile().getAddress(),
                        userPetCare.getUserProfile().getVetProfile().getLicenseNumber(),
                        userPetCare.getUserProfile().getVetProfile().getProfessionalTitle(),
                        userPetCare.getUserProfile().getVetProfile().getInstitution(),
                        userPetCare.getUserProfile().getVetProfile().getSpecialty()))
                .orElseThrow(() -> new VeterinarianException(VET_NOT_FOUND));
    }


}
