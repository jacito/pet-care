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
 */

package com.mx.petcare.auth.controller;

import com.mx.petcare.auth.dto.external.UserAuthInfoResponse;
import com.mx.petcare.auth.dto.external.UserProfileResponse;
import com.mx.petcare.auth.dto.response.AuthResponse;
import com.mx.petcare.auth.security.JwtUtil;

import com.mx.petcare.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private WebClient webClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;


    @Test
    void login_successful() {
        System.out.println("TEST :: login_successful");
        String username = "user1";
        String password = "1234";
        String encodedPassword = "$2a$10$fakeHash";
        String token = "jwt-token";

        UserAuthInfoResponse authInfo = new UserAuthInfoResponse();
        authInfo.setId(1L);
        authInfo.setUsername(username);
        authInfo.setEncodedPassword(encodedPassword);
        authInfo.setRole("USER");

        UserProfileResponse profile = new UserProfileResponse();
        profile.setNameFull("Naruto Uzumaki ");

        // Mocks para obtener UserAuthInfoResponse
        WebClient.RequestHeadersUriSpec requestUserUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestUserHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseUserSpec = mock(WebClient.ResponseSpec.class);

        // Mocks para obtener UserProfileResponse
        WebClient.RequestHeadersUriSpec requestProfileUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestProfileHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseProfileSpec = mock(WebClient.ResponseSpec.class);


        when(webClient.get())
                .thenReturn(requestUserUriSpec)     // Primera llamada
                .thenReturn(requestProfileUriSpec); // Segunda llamada

        // Configuración para UserAuthInfoResponse
        when(requestUserUriSpec.uri(eq("/{username}"), eq(username)))
                .thenReturn(requestUserHeadersSpec);
        when(requestUserHeadersSpec.retrieve())
                .thenReturn(responseUserSpec);
        when(responseUserSpec.bodyToMono(UserAuthInfoResponse.class))
                .thenReturn(Mono.just(authInfo));

        // Configuración para UserProfileResponse
        when(requestProfileUriSpec.uri(eq("/details/{id}"), eq(1L)))
                .thenReturn(requestProfileHeadersSpec);
        when(requestProfileHeadersSpec.retrieve())
                .thenReturn(responseProfileSpec);
        when(responseProfileSpec.bodyToMono(UserProfileResponse.class))
                .thenReturn(Mono.just(profile));

        // Simula que la contraseña coincide
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // Simula la generación del token JWT
        when(jwtUtil.generateToken(username, 1L, "USER")).thenReturn(token);

        // Ejecuta el método login que se está probando
        AuthResponse response = authService.login(username, password);

        // Verificaciones
        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals("Naruto Uzumaki ", response.getFullName());

        System.out.println("User :: " + response.getFullName());
        System.out.println(response.getToken());
    }

    @Test
    void login_userNotFound() {
        System.out.println("TEST :: login_userNotFound");

        String username = "no-existe";

        WebClient.RequestHeadersUriSpec requestUserUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestUserHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseUserSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestUserUriSpec);

        when(requestUserUriSpec.uri(eq("/{username}"), eq(username)))
                .thenReturn(requestUserHeadersSpec);
        when(requestUserHeadersSpec.retrieve())
                .thenReturn(responseUserSpec);
        when(responseUserSpec.bodyToMono(UserAuthInfoResponse.class))
                .thenReturn(Mono.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.login(username, "1234");
        });

        assertEquals("Usuario no encontrado", ex.getMessage());

        System.out.printf(ex.getMessage());
    }

    @Test
    void login_incorrectPassword() {
        System.out.println("TEST :: login_incorrectPassword");

        String username = "user1";
        String rawPassword = "1234";
        String encodedPassword = "$2a$10$fakeHash";

        UserAuthInfoResponse authInfo = new UserAuthInfoResponse();
        authInfo.setId(1L);
        authInfo.setUsername(username);
        authInfo.setEncodedPassword(encodedPassword);
        authInfo.setRole("USER");

        // Mock para obtener UserAuthInfoResponse
        WebClient.RequestHeadersUriSpec requestUserUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestUserHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseUserSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestUserUriSpec);
        when(requestUserUriSpec.uri(eq("/{username}"), eq(username)))
                .thenReturn(requestUserHeadersSpec);
        when(requestUserHeadersSpec.retrieve())
                .thenReturn(responseUserSpec);
        when(responseUserSpec.bodyToMono(UserAuthInfoResponse.class))
                .thenReturn(Mono.just(authInfo));

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.login(username, rawPassword);
        });

        assertEquals("Contraseña incorrecta", ex.getMessage());

        System.out.println(ex.getMessage());
    }


}