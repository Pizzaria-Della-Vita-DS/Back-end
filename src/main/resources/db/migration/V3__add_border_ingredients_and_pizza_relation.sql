CREATE TABLE tb_borda_ingrediente (
    borda_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    PRIMARY KEY (borda_id, ingrediente_id),
    CONSTRAINT fk_borda_ingrediente_borda
        FOREIGN KEY (borda_id) REFERENCES tb_borda (id) ON DELETE CASCADE,
    CONSTRAINT fk_borda_ingrediente_ingrediente
        FOREIGN KEY (ingrediente_id) REFERENCES tb_ingrediente (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE tb_pizza
    ADD COLUMN borda_id BIGINT NULL;

UPDATE tb_pizza pizza
JOIN tb_borda borda
    ON pizza.codigo LIKE CONCAT('% - borda ', borda.nome)
SET pizza.borda_id = borda.id
WHERE pizza.borda_id IS NULL;

ALTER TABLE tb_pizza
    ADD CONSTRAINT fk_pizza_borda
        FOREIGN KEY (borda_id) REFERENCES tb_borda (id) ON DELETE SET NULL;
