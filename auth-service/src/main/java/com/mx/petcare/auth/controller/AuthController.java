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
 * Application: auth-service
 * Author: Jacito
 * Creation Date: 28/05/2025
 */

package com.mx.petcare.auth.controller;

import com.mx.petcare.auth.dto.response.AuthResponse;
import com.mx.petcare.auth.dto.response.ErrorResponse;
import com.mx.petcare.auth.dto.response.LoginRequest;
import com.mx.petcare.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/petcare/auth")
@Tag(name = "Authentication",
        description = "Handles user and veterinarian login operations and returns JWT tokens for secure access to the PetCare platform.")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;


    /**
     * Endpoint to login a user.
     * Receives a LoginRequest object with the username and password of the user.
     * If the credentials are correct, it returns a JWT token.
     * <p>
     * In case of invalid credentials, the response will be an error message.
     *
     * @param request the object containing the username and password to authenticate the user.
     * @return ResponseEntity AuthResponse with the JWT token if authentication is successful, or an ErrorResponse error message if credentials are invalid.
     */
    @Operation(
            summary = "Login user",
            description = "Authenticates a user with username and password. If successful, returns a JWT token to be used in subsequent requests."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("Login request: {}", request.getUsername());
        try {
            return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()));
        }
    }
}
