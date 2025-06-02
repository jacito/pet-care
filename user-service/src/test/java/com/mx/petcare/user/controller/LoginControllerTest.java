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
import com.mx.petcare.user.dto.authentication.UserAuthInfoResponse;
import com.mx.petcare.user.dto.authentication.UserProfileResponse;
import com.mx.petcare.user.exception.UserException;
import com.mx.petcare.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserAuthInfo_successful() {
        System.out.println("TEST :: getUserAuthInfo_successful");
        String username = "testuser";
        UserAuthInfoResponse mockResponse = new UserAuthInfoResponse();
        when(userService.getAuthInfoByUsername(username)).thenReturn(mockResponse);

        ResponseEntity<?> response = loginController.getUserAuthInfo(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getUserAuthInfo_userNotFound() {
        System.out.println("TEST :: getUserAuthInfo_userNotFound");
        String username = "unknown";
        when(userService.getAuthInfoByUsername(username)).thenThrow(new UserException("User not found"));

        ResponseEntity<?> response = loginController.getUserAuthInfo(username);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("User not found", error.getDetails());

        System.out.println(error.getDetails());
    }

    @Test
    void getUserAuthInfo_internalError() {
        System.out.println("TEST :: getUserAuthInfo_internalError");
        String username = "errorUser";
        when(userService.getAuthInfoByUsername(username)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = loginController.getUserAuthInfo(username);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertTrue(error.getDetails().contains("Unexpected error"));

        System.out.println(error.getDetails());
    }

    @Test
    void getUserDetailsById_successful() {
        System.out.println("TEST :: getUserDetailsById_successful");
        Long id = 1L;
        UserProfileResponse profileResponse = new UserProfileResponse();
        when(userService.getAuthInfoUserDetailById(id)).thenReturn(profileResponse);

        ResponseEntity<?> response = loginController.getUserDetailsById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileResponse, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getUserDetailsById_userNotFound() {
        System.out.println("TEST :: getUserDetailsById_userNotFound");
        Long id = 99L;
        when(userService.getAuthInfoUserDetailById(id)).thenThrow(new UserException("User not found"));

        ResponseEntity<?> response = loginController.getUserDetailsById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("User not found", error.getDetails());
        System.out.println(error.getDetails());
    }

    @Test
    void getUserDetailsById_internalError() {
        System.out.println("TEST :: getUserDetailsById_internalError");
        Long id = 500L;
        when(userService.getAuthInfoUserDetailById(id)).thenThrow(new RuntimeException("Fatal"));

        ResponseEntity<?> response = loginController.getUserDetailsById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertTrue(error.getDetails().contains("Fatal"));

        System.out.println(error.getDetails());
    }
}
