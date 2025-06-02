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

import com.mx.petcare.pet.dto.detail.UserDetailView;
import com.mx.petcare.pet.dto.summary.UserView;
import com.mx.petcare.pet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final WebClient webClient;

    @Override
    public UserView getUserViewById(Long id) {
        String token = getJwtTokenFromContext();
        return webClient.get()
                .uri("/user/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(UserView.class)
                .block();
    }

    @Override
    public UserDetailView getUserDetailViewById(Long id) {
        String token = getJwtTokenFromContext();
        return webClient.get()
                .uri("/user/detail/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(UserDetailView.class)
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
