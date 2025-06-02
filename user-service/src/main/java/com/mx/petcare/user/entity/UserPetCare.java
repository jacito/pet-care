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
 * Creation Date: 27/05/2025
 */

package com.mx.petcare.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_pet_care")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPetCare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @JoinColumn(name = "user_profile_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;
}
