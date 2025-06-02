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

import com.mx.petcare.user.dto.ErrorResponse;
import com.mx.petcare.user.dto.veterinarian.RegisterVetRequest;
import com.mx.petcare.user.dto.veterinarian.VetDetailResponse;
import com.mx.petcare.user.exception.VeterinarianException;
import com.mx.petcare.user.service.impl.VetServiceImpl;
import com.mx.petcare.user.dto.veterinarian.VetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.mx.petcare.user.commons.Constants.ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/petcare")
@Tag(name = "Veterinarian Management", description = "APIs related to veterinarian registration, management, and details.")
public class VetController {

    private static final Logger logger = LoggerFactory.getLogger(VetController.class);

    private final VetServiceImpl vetService;


    /**
     * Endpoint to retrieve detailed information about a specific veterinarian by their ID.
     * Accessible only to users with the 'USER' role.
     * Receives a RegisterVetRequest object with credentials, profile data, and veterinary credentials.
     *
     * @param request the object containing username, email, password, profile, and vet data.
     * @return ResponseEntity with a success message or a 409 Conflict if the user/email already exists.
     */
    @Operation(summary = "Register a new veterinarian", description = "Registers a new veterinarian to the system. " +
            "Checks if the username or email already exists and returns a conflict error if true.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Veterinarian registered successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Veterinarian registered successfully.")
                    )),
            @ApiResponse(responseCode = "409", description = "No user found with the given ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Veterinarian Conflict",
                                    summary = "Veterinarian exists",
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
    @PostMapping("/register/vet")
    public ResponseEntity<?> registerVet(@RequestBody RegisterVetRequest request) {
        logger.info("Vet registration attempt: {}", request.getUsername());
        try {
            if (vetService.userExists(request.getUsername(), request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username or email already exists.");
            }
            vetService.registerVet(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Veterinarian registered successfully.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }

    /**
     * Endpoint Retrieves a list of all registered veterinarians with basic information.
     * <p>
     * Accessible only to users with the role {@code USER}.
     * </p>
     *
     * @return a list of {@link VetResponse} containing basic information (ID, full name, and specialty)
     * of all veterinarians.
     */
    @Operation(summary = "Get all veterinarians", description = "Retrieves a list of all registered veterinarians with basic information. Accessible only to 'USER' role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all veterinarians.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = VetResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/veterinarians")
    public ResponseEntity<?> getAllVeterinarians() {
        logger.info("Getting all veterinarians");
        try {
            return ResponseEntity.ok(vetService.getAllVeterinarians());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }

    /**
     * Retrieves a summary of the profile of a specific veterinarian identified by their ID.
     * <p>
     * Accessible only to users with the role {@code USER}.
     * </p>
     *
     * @param id the ID of the veterinarian to retrieve
     * @return a {@link VetResponse} containing detailed summarized veterinarian information.
     * of the veterinarian
     * @throws VeterinarianException if the veterinarian is not found
     */
    @Operation(summary = "Get summary veterinarian by ID", description = "Retrieves a summary of a specific veterinarian's profile by their ID. Accessible only to 'USER' role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinarian profile summary retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VetResponse.class))),
            @ApiResponse(responseCode = "204", description = "No veterinarian found with the given ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "Veterinarian not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/veterinarian/{id}")
    public ResponseEntity<?> getVeterinarianById(@PathVariable Long id) {
        try {
            logger.info("Getting veterinarian by id: {}", id);
            return ResponseEntity.ok(vetService.getVeterinarianById(id));
        } catch (VeterinarianException e) {
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
     * Retrieves the complete profile of a specific veterinarian identified by their ID.
     * <p>
     * Accessible only to users with the role {@code USER}.
     * </p>
     *
     * @param id the ID of the veterinarian to retrieve
     * @return a {@link VetDetailResponse} containing detailed personal and professional information
     * of the veterinarian
     * @throws VeterinarianException if the veterinarian is not found
     */
    @Operation(summary = "Get complete veterinarian profile", description = "Retrieves the complete profile of a specific veterinarian by their ID. Accessible only to 'USER' role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complete Veterinarian profile retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VetDetailResponse.class))),
            @ApiResponse(responseCode = "204", description = "No veterinarian found with the given ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "Veterinarian not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/veterinarian/detail/{id}")
    public ResponseEntity<?> getVeterinarianDetailById(@PathVariable Long id) {

        logger.info("Getting veterinarian detail by id: {}", id);
        try {
            return ResponseEntity.ok(vetService.getVeterinarianDetailById(id));
        } catch (VeterinarianException e) {
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
