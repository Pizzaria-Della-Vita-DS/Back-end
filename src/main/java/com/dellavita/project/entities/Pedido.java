package com.dellavita.project.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List; // AQUI

import org.springframework.format.annotation.DateTimeFormat;

import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento; // AQUI
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType; // AQUI
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany; // AQUI
import jakarta.persistence.Table;

@Entity
@Table(name= "tb_pedido")
public class Pedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cliente", referencedColumnName = "nome")
	private Cliente cliente;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) // AQUI
	private List<Pizza> pizzas; // AQUI (Substitui o antigo String itens)
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Estado estado;
	
	@Enumerated(EnumType.STRING) // AQUI
	@Column(nullable = false) // AQUI
	private FormaPagamento forma_pagamento; // AQUI
	
	private String endereco_entrega; // AQUI
	
	private Double preco_total; // AQUI
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime data_hora; // AQUI
	
	public Pedido() {
	}
	
	public Pedido(Long id, Cliente cliente, List<Pizza> pizzas, Estado estado, FormaPagamento forma_pagamento, String endereco_entrega, Double preco_total, LocalDateTime data_hora) { // AQUI
		super();
		this.id = id;
		this.cliente = cliente;
		this.pizzas = pizzas; // AQUI
		this.estado = estado;
		this.forma_pagamento = forma_pagamento; // AQUI
		this.endereco_entrega = endereco_entrega; // AQUI
		this.preco_total = preco_total; // AQUI
		this.data_hora = data_hora; // AQUI
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Pizza> getPizzas() { // AQUI
		return pizzas; // AQUI
	} // AQUI

	public void setPizzas(List<Pizza> pizzas) { // AQUI
		this.pizzas = pizzas; // AQUI
	} // AQUI

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public FormaPagamento getForma_pagamento() { // AQUI
		return forma_pagamento; // AQUI
	} // AQUI

	public void setForma_pagamento(FormaPagamento forma_pagamento) { // AQUI
		this.forma_pagamento = forma_pagamento; // AQUI
	} // AQUI

	public String getEndereco_entrega() { // AQUI
		return endereco_entrega; // AQUI
	} // AQUI

	public void setEndereco_entrega(String endereco_entrega) { // AQUI
		this.endereco_entrega = endereco_entrega; // AQUI
	} // AQUI

	public Double getPreco_total() { // AQUI
		return preco_total; // AQUI
	} // AQUI

	public void setPreco_total(Double preco_total) { // AQUI
		this.preco_total = preco_total; // AQUI
	} // AQUI

	public LocalDateTime getData_hora() { // AQUI
		return data_hora; // AQUI
	} // AQUI

	public void setData_hora(LocalDateTime data_hora) { // AQUI
		this.data_hora = data_hora; // AQUI
	} // AQUI

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", cliente=" + cliente + ", estado=" + estado + ", preco_total=" + preco_total + ", data_hora=" + data_hora + "]"; // AQUI
	}
}