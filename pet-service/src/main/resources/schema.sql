CREATE TABLE pets (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255),
                      species VARCHAR(255),
                      breed VARCHAR(255),
                      birth_date DATE,
                      weight DOUBLE,
                      gender VARCHAR(20),
                      user_id BIGINT,
                      vet_id BIGINT
);
