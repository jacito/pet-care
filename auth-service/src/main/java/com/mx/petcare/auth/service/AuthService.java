package com.mx.petcare.auth.service;

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


import com.mx.petcare.auth.dto.external.UserAuthInfoResponse;
import com.mx.petcare.auth.dto.external.UserProfileResponse;
import com.mx.petcare.auth.dto.response.AuthResponse;
import com.mx.petcare.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final WebClient webClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(String username, String password) {
        logger.debug("Login :: {}", username);

        UserAuthInfoResponse authInfo = webClient.get()
                .uri("/{username}", username)
                .retrieve()
                .bodyToMono(UserAuthInfoResponse.class)
                .block();

        if (authInfo == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        logger.debug("Usuario encontrado :: {} - {}", authInfo.getId(), authInfo.getUsername());

        if (!passwordEncoder.matches(password, authInfo.getEncodedPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        UserProfileResponse profile = webClient.get()
                .uri("/details/{id}", authInfo.getId())
                .retrieve()
                .bodyToMono(UserProfileResponse.class)
                .block();

        if (profile == null) {
            throw new RuntimeException("Perfil no encontrado");
        }

        String token = jwtUtil.generateToken(authInfo.getUsername(), authInfo.getId(), authInfo.getRole());
        return new AuthResponse(token, profile.getNameFull());
    }
}

