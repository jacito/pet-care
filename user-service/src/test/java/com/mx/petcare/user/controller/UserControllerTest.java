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

import static org.junit.jupiter.api.Assertions.*;

import com.mx.petcare.user.dto.ErrorResponse;
import com.mx.petcare.user.dto.user.RegisterUserRequest;
import com.mx.petcare.user.dto.user.UserDetailResponse;
import com.mx.petcare.user.dto.user.UserResponse;
import com.mx.petcare.user.exception.UserException;
import com.mx.petcare.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;


class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_successful() {
        System.out.println("TEST :: registerUser_successful");
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");

        when(userService.userExists("john", "john@example.com")).thenReturn(false);

        ResponseEntity<?> response = userController.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody());
        verify(userService).registerUser(request);

        System.out.println(response.getBody());
    }

    @Test
    void registerUser_conflict() {
        System.out.println("TEST :: registerUser_conflict");
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");

        when(userService.userExists("john", "john@example.com")).thenReturn(true);

        ResponseEntity<?> response = userController.registerUser(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username or email already exists.", response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getAllUsers_successful() {
        System.out.println("TEST :: getAllUsers_successful");
        List<UserResponse> userList = List.of(new UserResponse());
        when(userService.getAllStandardUsers()).thenReturn(userList);

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getAllUsers_internalServerError() {
        System.out.println("TEST :: getAllUsers_internalServerError");
        when(userService.getAllStandardUsers()).thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertTrue(error.getDetails().contains("DB Error"));

        System.out.println(error.getDetails());
    }

    @Test
    void getUserById_successful() {
        System.out.println("TEST :: getUserById_successful");
        Long id = 1L;
        UserResponse user = new UserResponse();
        when(userService.getUserById(id)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getUserById_userNotFound() {
        System.out.println("TEST :: getUserById_userNotFound");
        Long id = 1L;
        when(userService.getUserById(id)).thenThrow(new UserException("User not found"));

        ResponseEntity<?> response = userController.getUserById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("User not found", error.getDetails());

        System.out.println(error.getDetails());
    }

    @Test
    void getUserDetailById_successful() {
        System.out.println("TEST :: getUserDetailById_successful");
        Long id = 1L;
        UserDetailResponse userDetail = new UserDetailResponse();
        when(userService.getUserDetailById(id)).thenReturn(userDetail);

        ResponseEntity<?> response = userController.getUserDetailById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDetail, response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    void getUserDetailById_userNotFound() {
        System.out.println("TEST :: getUserDetailById_userNotFound");
        Long id = 1L;
        when(userService.getUserDetailById(id)).thenThrow(new UserException("User not found"));

        ResponseEntity<?> response = userController.getUserDetailById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("User not found", error.getDetails());

        System.out.println(error.getDetails());
    }
}

