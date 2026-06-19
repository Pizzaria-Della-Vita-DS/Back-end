package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.List; // AQUI

import com.dellavita.project.enums.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany; // AQUI
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore; // AQUI

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
	
	@JsonIgnore // AQUI: Evita loop infinito no JSON ao renderizar as duas pontas do N:M
	@ManyToMany(mappedBy = "ingredientes") // AQUI
	private List<Sabor> sabores; // AQUI
	
	public Ingrediente() {
	}
	
	public Ingrediente(Long id, String nome, Categoria categoria, boolean disponivel) { // AQUI
		super();
		this.id = id;
		this.nome = nome;
		this.categoria = categoria;
		this.disponivel = disponivel;
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

	public List<Sabor> getSabores() { // AQUI
		return sabores; // AQUI
	} // AQUI

	public void setSabores(List<Sabor> sabores) { // AQUI
		this.sabores = sabores; // AQUI
	} // AQUI

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Ingrediente [id=" + id + ", nome=" + nome + ", disponivel=" + disponivel + "]";
	}	
}