-- Usuario 1
INSERT INTO user_pet_care (id, username, password, role) --passwordPrueba
VALUES (1, 'prueba', '$2a$10$2KV7zrDvXOfbwMx8CNDlF.ygtexoNRbyTBYr3K4DRTdW25IcqbquC', 'USER');
INSERT INTO user_profile (id, first_name, middle_name, last_name, second_last_name, phone_number, email, address, user_id)
VALUES (1, 'Prueba', 'Test', 'Usuario', 'De Pruebas', '0000000000', 'prueba@correo.prueba.com.mx',
        'Calle Falsa 123, Colonia Inventada, Ciudad Imaginaria, CP 00000, País de Nunca Jamás', 1);

-- Usuario 2
INSERT INTO user_pet_care (id, username, password, role)--PruebasJacito
VALUES (2, 'jacito', '$2a$10$A3UVjv9E0QSWflaw5svVe.J8mEKTfCF.IN6N9DOOMCNEm81uds88i', 'USER');
INSERT INTO user_profile (id, first_name, last_name, second_last_name, phone_number, email, address, user_id)
VALUES (2, 'Jazmín', 'Velázquez', 'Bustos', '0000000001', 'jacitojvb@gmail.com',
        'Av. de los Sueños 456, Barrio Fantasía, Madrid, 28000, España', 2);

-- Veterinario
INSERT INTO user_pet_care (id, username, password, role) --secureVetPass1
VALUES (3, 'drjohnpetcare', '$2a$10$tqww3ZzRRLcmFJFFAbitfug4CPxsTCRfKkiPRi.zHVwl7yaN3dLvm', 'VET');
INSERT INTO user_profile (id, first_name, middle_name, last_name, second_last_name, phone_number, email, address, user_id)
VALUES (3, 'John', 'Edward', 'Doe', 'Miller', '555-2345', 'john.doe@petclinic.com',
        '1234 Maple Drive, Pleasantville, CA, 99999', 3);
INSERT INTO vet_profile (id, license_number, professional_title, institution, specialty, profile_id)
VALUES (1, 'VET9988776', 'Veterinary Surgeon', 'University of California Veterinary School', 'Exotic Animal Medicine',
        3);

