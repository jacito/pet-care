package com.mx.petcare.user.entity;

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

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vet_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String professionalTitle;

    private String institution;

    private String specialty;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;
}
