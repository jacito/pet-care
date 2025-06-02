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

package com.mx.petcare.user.service;

import com.mx.petcare.user.dto.authentication.UserAuthInfoResponse;
import com.mx.petcare.user.dto.authentication.UserProfileResponse;
import com.mx.petcare.user.dto.user.RegisterUserRequest;
import com.mx.petcare.user.dto.user.UserDetailResponse;
import com.mx.petcare.user.dto.user.UserResponse;

import java.util.List;


public interface UserService {

    boolean userExists(String username, String email);

    void registerUser(RegisterUserRequest request);

    UserDetailResponse getUserDetailById(Long id);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllStandardUsers();

    UserAuthInfoResponse getAuthInfoByUsername(String username);

    UserProfileResponse getAuthInfoUserDetailById(Long id);
}

