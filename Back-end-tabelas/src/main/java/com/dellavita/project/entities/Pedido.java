package com.dellavita.project.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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


    // é a relação de Cliente faz Pedido (N:1)
	@ManyToOne
	@JoinColumn(name = "cpf_cliente_fk")
	private Cliente cliente;

	// é a relação de Pedido tem Pizza (1:N)
	@OneToMany(mappedBy = "pedido")
	private Set<Pizza> pizzas = new HashSet<>();

	// é da tabela de Funcionario Atende Pedido (M:N)
	@ManyToMany(mappedBy = "pedidosAtendidos")
	private Set<Funcionario> funcionarios = new HashSet<>();


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

