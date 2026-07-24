-- Remove enums legados e permite que os valores sejam controlados pelas enums Java.
-- A conversao para VARCHAR preserva os valores ja cadastrados.
ALTER TABLE tb_funcionario
    MODIFY COLUMN funcao VARCHAR(255) NOT NULL,
    MODIFY COLUMN status VARCHAR(255) NOT NULL;
