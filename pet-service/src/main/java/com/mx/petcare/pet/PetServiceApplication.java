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
 * Creation Date: 27/05/2025
 */

package com.mx.petcare.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class PetServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(PetServiceApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Pets Service Application");
        SpringApplication.run(PetServiceApplication.class, args);
    }

}
