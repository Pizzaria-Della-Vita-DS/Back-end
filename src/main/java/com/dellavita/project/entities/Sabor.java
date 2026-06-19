package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; // AQUI
import jakarta.persistence.JoinTable; // AQUI
import jakarta.persistence.ManyToMany; // AQUI
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_sabor")
public class Sabor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome; // AQUI
	
	@ManyToMany // AQUI
	@JoinTable( // AQUI
		name = "tb_possui", // AQUI: Resolve a tabela intermediária do DER automaticamente
		joinColumns = @JoinColumn(name = "sabor_id"), // AQUI
		inverseJoinColumns = @JoinColumn(name = "ingrediente_id") // AQUI
	) // AQUI
	private List<Ingrediente> ingredientes;
	
	private Double preco;
	
	public Sabor() {
	}   
	
	public Sabor(Long id, String nome, List<Ingrediente> ingredientes, Double preco) { // AQUI
		super();
		this.id = id;
		this.nome = nome; // AQUI
		this.ingredientes = ingredientes;
		this.preco = preco;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() { // AQUI
		return nome; // AQUI
	} // AQUI

	public void setNome(String nome) { // AQUI
		this.nome = nome; // AQUI
	} // AQUI

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "Sabores [id=" + id + ", nome=" + nome + ", preco=" + preco + "]"; // AQUI
	}	
}