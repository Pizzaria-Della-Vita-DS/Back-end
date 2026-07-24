
package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	
	@Column(unique = true)
	private String nome; // AQUI
	
	@ManyToMany(fetch = FetchType.EAGER)  // era @ManyToMany (implicitamente LAZY)
	@JoinTable(
	    name = "tb_possui",
	    joinColumns = @JoinColumn(name = "sabor_id"),
	    inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
	)
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