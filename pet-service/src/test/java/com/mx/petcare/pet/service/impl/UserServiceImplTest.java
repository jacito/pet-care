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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mx.petcare.pet.dto.detail.UserDetailView;
import com.mx.petcare.pet.dto.summary.UserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.*;

import reactor.core.publisher.Mono;

public class UserServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockSecurityContextWithToken(String tokenValue) {
        var jwt = mock(org.springframework.security.oauth2.jwt.Jwt.class);
        when(jwt.getTokenValue()).thenReturn(tokenValue);

        JwtAuthenticationToken jwtAuth = mock(JwtAuthenticationToken.class);
        when(jwtAuth.getToken()).thenReturn(jwt);

        Authentication auth = jwtAuth;
        var securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getUserViewById_shouldReturnUserView() {
        Long userId = 1L;
        String token = "mocked-token";

        mockSecurityContextWithToken(token);

        UserView expectedUserView = new UserView();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/user/{id}", userId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq("Authorization"), eq("Bearer " + token))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserView.class)).thenReturn((Mono) Mono.just(expectedUserView));

        UserView result = userService.getUserViewById(userId);

        assertNotNull(result);
        assertEquals(expectedUserView, result);

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/user/{id}", userId);
        verify(requestHeadersSpec).header("Authorization", "Bearer " + token);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(UserView.class);
    }

    @Test
    void getUserDetailViewById_shouldReturnUserDetailView() {
        Long userId = 2L;
        String token = "mocked-token";

        mockSecurityContextWithToken(token);

        UserDetailView expectedDetailView = new UserDetailView();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/user/detail/{id}", userId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq("Authorization"), eq("Bearer " + token))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDetailView.class)).thenReturn((Mono) Mono.just(expectedDetailView));

        UserDetailView result = userService.getUserDetailViewById(userId);

        assertNotNull(result);
        assertEquals(expectedDetailView, result);

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/user/detail/{id}", userId);
        verify(requestHeadersSpec).header("Authorization", "Bearer " + token);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(UserDetailView.class);
    }
}
