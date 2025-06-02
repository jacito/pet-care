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

package com.mx.petcare.pet.controller;

import com.mx.petcare.pet.dto.*;
import com.mx.petcare.pet.dto.detail.PetUserDetailView;
import com.mx.petcare.pet.dto.detail.PetVetDetailView;
import com.mx.petcare.pet.dto.summary.PetVetView;
import com.mx.petcare.pet.exception.PetException;
import com.mx.petcare.pet.service.PetService;
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
import org.springframework.web.bind.annotation.*;

import static com.mx.petcare.pet.commons.Constants.ERROR;

/**
 * REST controller for handling pet-related operations.
 * Part of the PetCare project.
 */
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Pet Controller", description = "Handles operations related to pet registration, assignment and profile retrieval")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;

    /**
     * Endpoint to create a new pet record.
     * Accessible to authenticated users with appropriate permissions.
     *
     * @param request the CreatePetRequest containing pet details such as name, species, breed, etc.
     * @return ResponseEntity with a success message or a 409 Conflict if the name pet already exists.
     */
    @Operation(summary = "Register a new pet", description = "Creates a new pet record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet registered successfully.",
                    content = @Content(schema = @Schema(type = "string", example = "Pet registered successfully."))),
            @ApiResponse(responseCode = "409", description = "Pet already exists.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Pet Conflict",
                                    summary = "Pet exists",
                                    value = """
                                            {
                                                "code": 409,
                                                "message": "Conflict",
                                                "details": "Pet already exists."
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> createPet(@RequestBody CreatePetRequest request) {
        logger.info("Request to create pet: {}", request);

        try {
            if (petService.petExists(request.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Pet already exists.");
            }
            petService.createPet(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Pet registered successfully.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }

    /**
     * Endpoint to retrieve a list of pets registered by a specific user.
     * Typically used by pet owners to view their pets.
     *
     * @param userId the ID of the user whose pets are being retrieved.
     * @return a list of {@link PetVetView} objects representing each pet and its assigned vet (if any).
     */
    @Operation(summary = "Get pets by user ID", description = "Retrieves all pets registered by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PetVetView.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPetsByUser(@PathVariable Long userId) {
        logger.info("Request to get all pets by user: {}", userId);
        try {
            return ResponseEntity.ok(petService.getPetsByUserId(userId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }


    /**
     * Endpoint to retrieve a list of pets assigned to a specific veterinarian.
     * Used by veterinarians to view their assigned patients.
     *
     * @param vetId the ID of the veterinarian.
     * @return a list of {@link PetVetView}  objects representing each pet and its owner.
     */
    @Operation(summary = "Get pets by veterinarian ID", description = "Retrieves all pets assigned to a specific vet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of pets",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PetVetView.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/vet/{vetId}")
    public ResponseEntity<?> getPetsByVet(@PathVariable Long vetId) {
        logger.info("Request to get all pets by vet: {}", vetId);
        try {
            return ResponseEntity.ok(petService.getPetsByVetId(vetId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }

    /**
     * Endpoint to retrieve detailed pet and user information.
     * Used by pet owners to view their pet’s full profile and their own details.
     *
     * @param petId  the ID of the pet.
     * @param userId the ID of the user who owns the pet.
     * @return ResponseEntity  a {@link PetUserDetailView}containing pet and user details.
     */
    @Operation(summary = "Get detailed pet and user info", description = "Returns detailed information of a pet and its owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet and user detail",
                    content = @Content(schema = @Schema(implementation = PetUserDetailView.class))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "Pet not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{petId}/user/{userId}")
    public ResponseEntity<?> getPetDetailForUser(@PathVariable Long petId, @PathVariable Long userId) {
        logger.info("Request to get pet detail for user: {}", userId);
        try {
            return ResponseEntity.ok(petService.getPetDetailsForUser(petId, userId));
        } catch (PetException e) {
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
     * Endpoint to retrieve detailed pet and vet information.
     * Used by veterinarians to view the complete profile of a pet and their own vet data.
     *
     * @param petId the ID of the pet.
     * @param vetId the ID of the veterinarian.
     * @return ResponseEntity a {@link PetVetDetailView} containing pet and vet details.
     */
    @Operation(summary = "Get detailed pet and vet info", description = "Returns detailed information of a pet and its assigned veterinarian.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet and vet detail",
                    content = @Content(schema = @Schema(implementation = PetVetDetailView.class))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "Pet not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{petId}/vet/{vetId}")
    public ResponseEntity<?> getPetDetailForVet(@PathVariable Long petId, @PathVariable Long vetId) {
        logger.info("Request to get pet detail for vet: {}", vetId);
        try {
            return ResponseEntity.ok(petService.getPetDetailsForVet(petId, vetId));
        } catch (PetException e) {
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
     * Endpoint to assign a veterinarian to a specific pet.
     * Used to link an existing pet record with a registered veterinarian.
     *
     * @param request the AssignVetRequest object containing the petId and vetId.
     * @return ResponseEntity with a success message confirming the assignment.
     */
    @Operation(summary = "Assign a vet to a pet", description = "Links an existing pet with a registered veterinarian.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vet assigned",
                    content = @Content(schema = @Schema(type = "string", example = "Veterinarian assigned successfully"))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "No Found",
                                    summary = "No such object found",
                                    value = """
                                            {
                                                "code": 204,
                                                "message": "No Content",
                                                "details": "Pet not found"
                                            }
                                            """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/assign-vet")
    public ResponseEntity<?> assignVet(@RequestBody AssignVetRequest request) {
        logger.info("Request to assign vet: {}", request);
        try {
            petService.assignVeterinarian(request.getPetId(), request.getVetId());
            return ResponseEntity.ok("Veterinarian assigned successfully");
        } catch (PetException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new ErrorResponse(HttpStatus.NO_CONTENT.value(),
                            HttpStatus.NO_CONTENT.getReasonPhrase(), e.getMessage()));
        } catch (
                Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            ERROR + e.getMessage()));
        }
    }

}
