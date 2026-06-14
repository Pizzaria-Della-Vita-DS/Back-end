package com.dellavita.project.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;


@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    private Estado estado;
    private FormaPagamento forma_pagamento;
    private LocalDateTime data_hora;
    private Integer preco_total;
    @ManyToOne
    @JoinColumn(name = "cpf_cliente_fk")
    private Cliente cliente;

    

    public Pedido() {
    }

    public Pedido(long codigo, Estado estado, FormaPagamento forma_pagamento, LocalDateTime data_hora,
            Integer preco_total, Cliente cliente) {
        this.codigo = codigo;
        this.estado = estado;
        this.forma_pagamento = forma_pagamento;
        this.data_hora = data_hora;
        this.preco_total = preco_total;
        this.cliente = cliente;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public FormaPagamento getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(FormaPagamento forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }

    public LocalDateTime getData_hora() {
        return data_hora;
    }

    public void setData_hora(LocalDateTime data_hora) {
        this.data_hora = data_hora;
    }

    public Integer getPreco_total() {
        return preco_total;
    }

    public void setPreco_total(Integer preco_total) {
        this.preco_total = preco_total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}

