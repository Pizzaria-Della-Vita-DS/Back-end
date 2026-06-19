package com.dellavita.project.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.dellavita.project.enums.Estado;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private String itens;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Estado estado;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime horario;
	
	public Pedido() {
	}
	
	
	public Pedido(Long id, Cliente cliente, String itens, Estado estado, LocalDateTime horario) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.itens = itens;
		this.estado = estado;
		this.horario = horario;
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


	public String getItens() {
		return itens;
	}


	public void setItens(String itens) {
		this.itens = itens;
	}


	public Estado getEstado() {
		return estado;
	}


	public void setEstado(Estado estado) {
		this.estado = estado;
	}


	public LocalDateTime getHorario() {
		return horario;
	}


	public void setHorario(LocalDateTime horario) {
		this.horario = horario;
	}


	@Override
	public String toString() {
		return "Pedido [id=" + id + ", cliente=" + cliente + ", itens=" + itens + ", horario=" + horario + "]";
	}
	

}
