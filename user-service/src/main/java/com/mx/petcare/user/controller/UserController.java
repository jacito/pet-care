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

package com.mx.petcare.user.controller;

import com.mx.petcare.user.dto.*;
import com.mx.petcare.user.dto.user.RegisterUserRequest;
import com.mx.petcare.user.dto.user.UserDetailResponse;
import com.mx.petcare.user.dto.user.UserResponse;
import com.mx.petcare.user.exception.UserException;
import com.mx.petcare.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.mx.petcare.user.commons.Constants.ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/petcare")
@Tag(name = "User Management", description = "Operations related to user management including registration and profile retrieval.")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    /**
     * Endpoint to retrieve a list of all registered veterinarians.
     * Accessible only to users with the 'USER' role.
     * Receives a RegisterUserRequest object with the user's credentials and personal profile data.
     *
     * @param request the object containing username, email, password, and profile details.
     * @return ResponseEntity with a success message or a 409 Conflict if the user/email already exists.
     */
    @Operation(summary = "Register a new user", description = "Registers a new user in the system, if the username or email doesn't already exist.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "User registered successfully.")
                    )),
            @ApiResponse(responseCode = "409", description = "No user found with the given ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "User Conflict",
                                    summary = "User exists",
                                    value = """
                                            {
                                                "code": 409,
                                                "message": "Conflict",
                                                "details": "Username or email already exists."
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        logger.info("User registration attempt: {}", request.getUsername());
        try {
            if (userService.userExists(request.getUsername(), request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username or email already exists.");
            }
            userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            "Unexpected error: " + e.getMessage()));
        }
    }


    /**
     * Endpoint Retrieves a list of all registered user with basic information.
     * <p>
     * Accessible only to users with the role {@code VET}.
     * </p>
     *
     * @return a list of {@link UserResponse} containing basic information (ID and full name) of all users.
     */
    @Operation(summary = "Get all registered users", description = "Retrieves a list of all registered users with basic information, accessible only by VET users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VET')")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        logger.info("Getting all users (non-veterinarians)");
        try {
            return ResponseEntity.ok(userService.getAllStandardUsers());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }


    /**
     * Retrieves a summary of the profile of a specific user identified by their ID.
     * <p>
     * Accessible only to users with the role {@code VET}.
     * </p>
     *
     * @param id the ID of the user to retrieve
     * @return a {@link UserResponse} containing summarized user information.
     * @throws UserException if the user is not found
     */
    @Operation(summary = "Get summary user by ID", description = "Retrieves the profile summary of a user by their ID. Accessible only by VET users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile summary retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "204", description = "No user found with the given ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "User not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VET')")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Getting user by id: {}", id);
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (UserException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new ErrorResponse(HttpStatus.NO_CONTENT.value(),
                            HttpStatus.NO_CONTENT.getReasonPhrase(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }


    /**
     * Retrieves the complete profile of a specific user identified by their ID.
     * <p>
     * Accessible only to users with the role {@code VET}.
     * </p>
     *
     * @param id the ID of the veterinarian to retrieve
     * @return a {@link UserDetailResponse} containing detailed personal
     * @throws UserException if the user is not found
     */
    @Operation(summary = "Get full user profile by ID", description = "Retrieves the complete profile of a specific user by their ID. Accessible only by VET users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complete user profile retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailResponse.class))),
            @ApiResponse(responseCode = "204", description = "No user found with the given ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "User not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('VET')")
    @GetMapping("/user/detail/{id}")
    public ResponseEntity<?> getUserDetailById(@PathVariable Long id) {
        logger.info("Getting user detail by id: {}", id);
        try {
            return ResponseEntity.ok(userService.getUserDetailById(id));
        } catch (UserException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new ErrorResponse(HttpStatus.NO_CONTENT.value(),
                            HttpStatus.NO_CONTENT.getReasonPhrase(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }
}
