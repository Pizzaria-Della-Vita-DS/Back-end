

-- ============================================================
-- V1: Schema inicial do banco Della Vita
-- ============================================================

-- Cliente e Funcionário: tabelas próprias, pois Usuario é @MappedSuperclass
CREATE TABLE tb_cliente (
    cpf VARCHAR(11) NOT NULL,
    nome VARCHAR(255),
    login VARCHAR(255),
    senha VARCHAR(255),
    genero VARCHAR(255),
    telefone VARCHAR(255),
    endereco VARCHAR(255),
    PRIMARY KEY (cpf),
    -- necessário pois tb_pedido referencia Cliente pelo campo "nome", não pelo cpf (ver observação abaixo)
    UNIQUE KEY uk_cliente_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_funcionario (
    cpf VARCHAR(11) NOT NULL,
    nome VARCHAR(255),
    login VARCHAR(255),
    senha VARCHAR(255),
    genero VARCHAR(255),
    telefone VARCHAR(255),
    funcao VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    rg VARCHAR(255),
    data_nascimento DATE,
    setor VARCHAR(255),
    PRIMARY KEY (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_ingrediente (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255),
    disponivel TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_sabor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255),
    preco DOUBLE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Relação N:N Sabor <-> Ingrediente
CREATE TABLE tb_possui (
    sabor_id BIGINT NOT NULL,
    ingrediente_id BIGINT NOT NULL,
    PRIMARY KEY (sabor_id, ingrediente_id),
    CONSTRAINT fk_possui_sabor FOREIGN KEY (sabor_id) REFERENCES tb_sabor (id),
    CONSTRAINT fk_possui_ingrediente FOREIGN KEY (ingrediente_id) REFERENCES tb_ingrediente (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_borda (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255),
    preco DOUBLE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_pedido (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente VARCHAR(255),
    estado VARCHAR(255) NOT NULL,
    forma_pagamento VARCHAR(255) NOT NULL,
    endereco_entrega VARCHAR(255),
    preco_total DOUBLE,
    data_hora DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente) REFERENCES tb_cliente (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_pizza (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(255),
    preco DOUBLE,
    tamanho VARCHAR(255),
    pedido_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_pizza_pedido FOREIGN KEY (pedido_id) REFERENCES tb_pedido (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

