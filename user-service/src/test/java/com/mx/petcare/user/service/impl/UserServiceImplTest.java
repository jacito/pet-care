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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.mx.petcare.user.dto.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void userExists_shouldReturnTrueIfUsernameOrEmailExists() {
        when(userRepository.existsByUsername("user1")).thenReturn(true);
        when(userProfileRepository.existsByEmail("test@mail.com")).thenReturn(false);

        boolean exists = userService.userExists("user1", "test@mail.com");

        assertTrue(exists);
    }

    @Test
    void registerUser_shouldSaveUserWithEncodedPassword() {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .username("testuser")
                .password("1234")
                .firstName("Naruto")
                .email("ninja@leaf.com")
                .build();

        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

        userService.registerUser(request);

        verify(userRepository).save(any(UserPetCare.class));
    }

    @Test
    void getUserDetailById_shouldReturnUserDetail() {
        UserProfile profile = UserProfile.builder()
                .firstName("Sasuke")
                .email("sasuke@uchiha.com")
                .phoneNumber("1234567890")
                .address("Konoha")
                .build();

        UserPetCare user = UserPetCare.builder()
                .id(1L)
                .role(USER.getRole())
                .userProfile(profile)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDetailResponse response = userService.getUserDetailById(1L);

        assertEquals("Sasuke", response.getFullName());
        assertEquals("sasuke@uchiha.com", response.getEmail());
    }

    @Test
    void getUserById_shouldThrowExceptionIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllStandardUsers_shouldReturnList() {
        UserPetCare user1 = UserPetCare.builder()
                .id(1L)
                .role(USER.getRole())
                .userProfile(UserProfile.builder().firstName("Sakura").build())
                .build();

        when(userRepository.findByRole(USER.getRole())).thenReturn(List.of(user1));

        List<UserResponse> list = userService.getAllStandardUsers();

        assertEquals(1, list.size());
    }

    @Test
    void getAuthInfoByUsername_shouldReturnData() {
        UserPetCare user = UserPetCare.builder()
                .id(1L)
                .username("user")
                .password("encoded")
                .role(USER.getRole())
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        UserAuthInfoResponse authInfo = userService.getAuthInfoByUsername("user");

        assertEquals("user", authInfo.getUsername());
        assertEquals("encoded", authInfo.getEncodedPassword());
    }

    @Test
    void getAuthInfoUserDetailById_shouldReturnProfile() {
        UserProfile profile = UserProfile.builder()
                .firstName("Kakashi")
                .email("kakashi@leaf.com")
                .build();

        UserPetCare user = UserPetCare.builder()
                .id(10L)
                .userProfile(profile)
                .build();

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        UserProfileResponse response = userService.getAuthInfoUserDetailById(10L);

        assertEquals("Kakashi", response.getNameFull());
        assertEquals("kakashi@leaf.com", response.getEmail());
    }
}
