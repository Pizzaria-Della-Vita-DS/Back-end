package com.dellavita.project.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_borda")
public class Borda implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private Double preco; // AQUI
	
	public Borda() {
	}
	
	public Borda(Long id, String nome, Double preco) { // AQUI
		super();
		this.id = id;
		this.nome = nome;
		this.preco = preco; // AQUI
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

	public Double getPreco() { // AQUI
		return preco; // AQUI
	} // AQUI

	public void setPreco(Double preco) { // AQUI
		this.preco = preco; // AQUI
	} // AQUI

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Borda [id=" + id + ", nome=" + nome + ", preco=" + preco + "]"; // AQUI
	}
}