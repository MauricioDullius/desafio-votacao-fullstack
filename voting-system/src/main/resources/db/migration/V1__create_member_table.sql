CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        cpf VARCHAR(11) NOT NULL,
                        active BOOLEAN NOT NULL
);