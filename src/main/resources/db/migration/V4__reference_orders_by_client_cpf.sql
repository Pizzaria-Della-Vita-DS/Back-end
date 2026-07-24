ALTER TABLE tb_pedido
    ADD COLUMN cliente_cpf VARCHAR(11) NULL;

UPDATE tb_pedido pedido
JOIN tb_cliente cliente ON cliente.nome = pedido.cliente
SET pedido.cliente_cpf = cliente.cpf;

SET @fk_pedido_cliente = (
    SELECT constraint_name
    FROM information_schema.key_column_usage
    WHERE constraint_schema = DATABASE()
      AND table_name = 'tb_pedido'
      AND referenced_table_name = 'tb_cliente'
    LIMIT 1
);
SET @sql_drop_fk_pedido_cliente = IF(
    @fk_pedido_cliente IS NULL,
    'SELECT 1',
    CONCAT('ALTER TABLE tb_pedido DROP FOREIGN KEY `', @fk_pedido_cliente, '`')
);
PREPARE stmt_drop_fk_pedido_cliente FROM @sql_drop_fk_pedido_cliente;
EXECUTE stmt_drop_fk_pedido_cliente;
DEALLOCATE PREPARE stmt_drop_fk_pedido_cliente;

ALTER TABLE tb_pedido
    DROP COLUMN cliente,
    MODIFY COLUMN cliente_cpf VARCHAR(11) NOT NULL,
    ADD CONSTRAINT fk_pedido_cliente_cpf
        FOREIGN KEY (cliente_cpf) REFERENCES tb_cliente (cpf) ON DELETE CASCADE;

SET @fk_pizza_pedido = (
    SELECT constraint_name
    FROM information_schema.key_column_usage
    WHERE constraint_schema = DATABASE()
      AND table_name = 'tb_pizza'
      AND referenced_table_name = 'tb_pedido'
    LIMIT 1
);
SET @sql_drop_fk_pizza_pedido = IF(
    @fk_pizza_pedido IS NULL,
    'SELECT 1',
    CONCAT('ALTER TABLE tb_pizza DROP FOREIGN KEY `', @fk_pizza_pedido, '`')
);
PREPARE stmt_drop_fk_pizza_pedido FROM @sql_drop_fk_pizza_pedido;
EXECUTE stmt_drop_fk_pizza_pedido;
DEALLOCATE PREPARE stmt_drop_fk_pizza_pedido;

ALTER TABLE tb_pizza
    ADD CONSTRAINT fk_pizza_pedido_cascade
        FOREIGN KEY (pedido_id) REFERENCES tb_pedido (id) ON DELETE CASCADE;
