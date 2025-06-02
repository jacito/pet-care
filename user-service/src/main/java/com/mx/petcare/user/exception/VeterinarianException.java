/**
 * Copyright © 2025 Jazmín Velázquez Bustos. All rights reserved.
 *
 * This file and its contents are protected by copyright law.
 * Reproduction, distribution, or use of any part of this software
 * without the prior written consent of the author is strictly prohibited.
 *
 * This code was developed as part of the final evaluation project
 * for the Java Microservices Bootcamp offered by Código Facilito.
 *
 * Project: PetCare - Pet Health and Wellness Management System
 * Application: user-service
 * Author: Jacito
 */

package com.mx.petcare.user.exception;

public class VeterinarianException extends RuntimeException {
  public VeterinarianException(String message) {
    super(message);
  }
}
