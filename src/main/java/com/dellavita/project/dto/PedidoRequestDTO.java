package com.dellavita.project.dto;

import java.util.ArrayList;
import java.util.List;

import com.dellavita.project.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PedidoRequestDTO {

    @JsonProperty("cliente_cpf")
    private String clienteCpf;

    private List<PizzaRequestDTO> pizzas = new ArrayList<>();

    @JsonProperty("forma_pagamento")
    private FormaPagamento formaPagamento;

    @JsonProperty("endereco_entrega")
    private String enderecoEntrega;

    @JsonProperty("preco_total")
    private Double precoTotal;

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public List<PizzaRequestDTO> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<PizzaRequestDTO> pizzas) {
        this.pizzas = pizzas;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public static class PizzaRequestDTO {

        private String tamanho;
        private Double preco;

        @JsonProperty("sabores_ids")
        private List<Long> saboresIds = new ArrayList<>();

        @JsonProperty("borda_id")
        private Long bordaId;

        public String getTamanho() {
            return tamanho;
        }

        public void setTamanho(String tamanho) {
            this.tamanho = tamanho;
        }

        public Double getPreco() {
            return preco;
        }

        public void setPreco(Double preco) {
            this.preco = preco;
        }

        public List<Long> getSaboresIds() {
            return saboresIds;
        }

        public void setSaboresIds(List<Long> saboresIds) {
            this.saboresIds = saboresIds;
        }

        public Long getBordaId() {
            return bordaId;
        }

        public void setBordaId(Long bordaId) {
            this.bordaId = bordaId;
        }
    }
}
