package com.dellavita.project.entities;

import java.io.Serializable;

import com.dellavita.project.enums.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_ingrediente")
public class Ingrediente implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Categoria categoria;
	private boolean disponivel;
	@ManyToOne
	@JoinColumn(name = "sabor_id")
	private Sabor sabor;
	
	public Ingrediente() {
	}
	
	public Ingrediente(Long id, String nome, Categoria categoria, boolean disponivel, Sabor sabor) {
		super();
		this.id = id;
		this.nome = nome;
		this.categoria = categoria;
		this.disponivel = disponivel;
		this.sabor = sabor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	@Override
	public String toString() {
		return "Ingrediente [id=" + id + ", nome=" + nome + ", disponivel=" + disponivel + "]";
	}
	
	
	
	
}
