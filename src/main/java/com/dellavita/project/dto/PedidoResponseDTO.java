package com.dellavita.project.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.dellavita.project.entities.Pedido;
import com.dellavita.project.entities.Pizza;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.enums.Tamanho;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PedidoResponseDTO {

    private Long id;

    @JsonProperty("cliente_cpf")
    private String clienteCpf;

    @JsonProperty("cliente_nome")
    private String clienteNome;

    private List<PizzaResponseDTO> pizzas;
    private Estado estado;

    @JsonProperty("forma_pagamento")
    private FormaPagamento formaPagamento;

    @JsonProperty("endereco_entrega")
    private String enderecoEntrega;

    @JsonProperty("preco_total")
    private Double precoTotal;

    @JsonProperty("data_hora")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;

    public static PedidoResponseDTO fromEntity(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.id = pedido.getId();
        if (pedido.getCliente() != null) {
            dto.clienteCpf = pedido.getCliente().getCpf();
            dto.clienteNome = pedido.getCliente().getNome();
        }
        dto.pizzas = pedido.getPizzas() == null
                ? Collections.emptyList()
                : pedido.getPizzas().stream().map(PizzaResponseDTO::fromEntity).toList();
        dto.estado = pedido.getEstado();
        dto.formaPagamento = pedido.getForma_pagamento();
        dto.enderecoEntrega = pedido.getEndereco_entrega();
        dto.precoTotal = pedido.getPreco_total();
        dto.dataHora = pedido.getData_hora();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public List<PizzaResponseDTO> getPizzas() {
        return pizzas;
    }

    public Estado getEstado() {
        return estado;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public static class PizzaResponseDTO {

        private Long id;
        private String codigo;
        private Double preco;
        private Tamanho tamanho;

        @JsonProperty("borda_id")
        private Long bordaId;

        @JsonProperty("borda_nome")
        private String bordaNome;

        public static PizzaResponseDTO fromEntity(Pizza pizza) {
            PizzaResponseDTO dto = new PizzaResponseDTO();
            dto.id = pizza.getId();
            dto.codigo = pizza.getCodigo();
            dto.preco = pizza.getPreco();
            dto.tamanho = pizza.getTamanho();
            if (pizza.getBorda() != null) {
                dto.bordaId = pizza.getBorda().getId();
                dto.bordaNome = pizza.getBorda().getNome();
            }
            return dto;
        }

        public Long getId() {
            return id;
        }

        public String getCodigo() {
            return codigo;
        }

        public Double getPreco() {
            return preco;
        }

        public Tamanho getTamanho() {
            return tamanho;
        }

        public Long getBordaId() {
            return bordaId;
        }

        public String getBordaNome() {
            return bordaNome;
        }
    }
}
