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

package com.mx.petcare.pet.service.impl;

import com.mx.petcare.pet.dto.detail.VetDetailView;
import com.mx.petcare.pet.dto.summary.VetView;
import com.mx.petcare.pet.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {
    private static final Logger logger = LoggerFactory.getLogger(VetServiceImpl.class);

    private final WebClient webClient;

    @Override
    public VetView getVetViewById(Long id) {
        logger.debug("getVetViewById :: {}", id);
        String token = getJwtTokenFromContext();
        return webClient.get()
                .uri("/veterinarian/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(VetView.class)
                .block();
    }

    @Override
    public VetDetailView getVetDetailViewById(Long id) {
        logger.debug("getVetDetailViewById :: {}", id);
        String token = getJwtTokenFromContext();
        return webClient.get()
                .uri("/veterinarian/detail/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(VetDetailView.class)
                .block();
    }

    private String getJwtTokenFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            return jwtAuthToken.getToken().getTokenValue();
        }
        return null;
    }
}
