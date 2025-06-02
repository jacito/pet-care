DROP TABLE IF EXISTS vet_profiles;
DROP TABLE IF EXISTS user_profile;
DROP TABLE IF EXISTS user_pet_care;

CREATE TABLE user_pet_care
(
    id       BIGINT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

CREATE TABLE user_profile
(
    id               BIGINT PRIMARY KEY,
    first_name       VARCHAR(50)  NOT NULL,
    middle_name      VARCHAR(50),
    last_name        VARCHAR(50)  NOT NULL,
    second_last_name VARCHAR(50),
    phone_number     VARCHAR(20),
    email            VARCHAR(100) NOT NULL UNIQUE,
    address          VARCHAR(255),
    user_id          BIGINT       NOT NULL,
    CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES user_pet_care (id)
);

CREATE TABLE vet_profile
(
    id                 BIGINT PRIMARY KEY,
    license_number     VARCHAR(50)  NOT NULL,
    professional_title VARCHAR(100) NOT NULL,
    institution        VARCHAR(100),
    specialty          VARCHAR(100),
    profile_id         BIGINT       NOT NULL,
    CONSTRAINT fk_vet_profile_user_profile FOREIGN KEY (profile_id) REFERENCES user_profile (id)
);
