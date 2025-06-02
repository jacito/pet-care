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

import com.mx.petcare.user.dto.authentication.UserAuthInfoResponse;
import com.mx.petcare.user.dto.authentication.UserProfileResponse;
import com.mx.petcare.user.dto.user.RegisterUserRequest;
import com.mx.petcare.user.dto.user.UserDetailResponse;
import com.mx.petcare.user.dto.user.UserResponse;
import com.mx.petcare.user.entity.UserPetCare;
import com.mx.petcare.user.entity.UserProfile;
import com.mx.petcare.user.exception.UserException;
import com.mx.petcare.user.repository.UserProfileRepository;
import com.mx.petcare.user.repository.UserRepository;
import com.mx.petcare.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.mx.petcare.user.commons.Constants.USER_NOT_FOUND;
import static com.mx.petcare.user.commons.Util.loadFullName;
import static com.mx.petcare.user.dto.Role.USER;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean userExists(String username, String email) {
        return userRepository.existsByUsername(username) || userProfileRepository.existsByEmail(email);
    }

    @Override
    public void registerUser(RegisterUserRequest request) {
        logger.debug("Register user :: {}", request.getUsername());

        UserProfile userProfile = UserProfile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .secondLastName(request.getSecondLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        UserPetCare user = UserPetCare.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(USER.getRole())
                .userProfile(userProfile)
                .build();

        userProfile.setUserpetCare(user);

        userRepository.save(user);

    }

    @Override
    public UserDetailResponse getUserDetailById(Long id) {
        return userRepository.findById(id)
                .filter(userPetCare -> USER.getRole().equals(userPetCare.getRole()))
                .map(userPetCare -> new UserDetailResponse(
                        userPetCare.getId(),
                        loadFullName(userPetCare.getUserProfile()),
                        userPetCare.getUserProfile().getEmail(),
                        userPetCare.getUserProfile().getPhoneNumber(),
                        userPetCare.getUserProfile().getAddress()))
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .filter(userPetCare -> USER.getRole().equals(userPetCare.getRole()))
                .map(userPetCare -> new UserResponse(
                        userPetCare.getId(),
                        loadFullName(userPetCare.getUserProfile())))
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Override
    public List<UserResponse> getAllStandardUsers() {
        return userRepository.findByRole(USER.getRole()).stream()
                .map(userPetCare -> new UserResponse(
                        userPetCare.getId(),
                        loadFullName(userPetCare.getUserProfile())))
                .collect(Collectors.toList());
    }

    @Override
    public UserAuthInfoResponse getAuthInfoByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userPetCare -> new UserAuthInfoResponse(
                        userPetCare.getId(),
                        userPetCare.getUsername(),
                        userPetCare.getPassword(),
                        userPetCare.getRole()))
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Override
    public UserProfileResponse getAuthInfoUserDetailById(Long id) {
        return userRepository.findById(id)
                .map(userPetCare -> new UserProfileResponse(
                        loadFullName(userPetCare.getUserProfile()),
                        userPetCare.getUserProfile().getEmail()) {
                })
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}

