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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.mx.petcare.user.commons.Constants.ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/petcare")
@Tag(name = "Authentication Information",
        description = "Endpoints used by auth-service to fetch user credentials and profile details")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserServiceImpl userService;

    /**
     * Endpoint to retrieve essential user authentication data by username.
     * Used by the auth-service to validate credentials and identify user roles.
     *
     * @param username the username provided during login.
     * @return ResponseEntity a {@link UserAuthInfoResponse} containing user ID, username, hashed password, and role.
     */
    @Operation(
            summary = "Get user authentication info by username",
            description = "Returns basic user authentication data including id, username, encoded password, and role. "
                    + "Used by auth-service during login."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authentication info retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthInfoResponse.class))),
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
    @GetMapping("/auth-info/{username}")
    public ResponseEntity<?> getUserAuthInfo(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getAuthInfoByUsername(username));
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
     * Endpoint to retrieve full user profile data by ID.
     * Called after successful login to enrich JWT token payload with user details.
     *
     * @param id the unique identifier of the user.
     * @return ResponseEntity {@link UserProfileResponse} containing user profile details including names and contact info,
     * or an ErrorResponse error message if there is a problem finding the user.
     */
    @Operation(
            summary = "Get full user profile by ID",
            description = "Returns detailed user profile data by user ID. Used after login to enrich JWT with profile information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
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
    @GetMapping("/auth-info/details/{id}")
    public ResponseEntity<?> getUserDetailsById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getAuthInfoUserDetailById(id));
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
